package pl.poznan.put.dogloverservice.modules.walk

import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ArrivedAtDestinationWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverAlreadyOnWalkException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkUpdateException
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.ARRIVED_AT_DESTINATION
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.CANCELED
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.LEFT_DESTINATION
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.ONGOING
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO

@Service
class WalkService(
        private val walkRepository: WalkRepository,
        private val dogLoverService: DogLoverService,
        private val dogService: DogService,
        private val mapMarkerService: MapMarkerService,
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {

    fun saveWalk(walkDTO: WalkDTO, dogLoverId: UUID): WalkDTO {
        validateDogLoverIsNotAlreadyOnWalk(dogLoverId)
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dogs = walkDTO.dogNames.map { dogService.getDogEntity(it, dogLoverId) }
        val mapMarker = mapMarkerService.getMapMarker(walkDTO.mapMarker)
        val walk = Walk(
                UUID.randomUUID(),
                Instant.now(),
                dogLover,
                dogs,
                mapMarker,
                ONGOING
        )
        return WalkDTO(walkRepository.save(walk))
    }

    fun updateWalkStatus(walkId: UUID, walkStatus: WalkStatus, dogLoverId: UUID): WalkDTO {
        val walk = getWalkByIdAndDogLoverId(walkId, dogLoverId)
        if (!isWalkStatusPermissible(currentWalkStatus = walk.walkStatus, newWalkStatus = walkStatus))
            throw WalkUpdateException()

        return WalkDTO(walkRepository.save(
                Walk(walk, walkStatus)))
    }

    fun getDogLoversInLocation(mapMarkerId: UUID, dogLoverId: UUID): List<DogLoverInLocationDTO> {
        val walks = walkRepository.findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(mapMarkerId, ARRIVED_AT_DESTINATION, dogLoverId)
        val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)
                .groupBy { it.dogLoverRelationshipId.receiverDogLover.id }

        return walks.map { walk ->
            DogLoverInLocationDTO(
                    walk.dogLover,
                    walk.dogs.map { DogBasicInfoDTO(it) },
                    dogLoverRelationships[walk.dogLover.id]?.first()?.relationshipStatus,
                    walk.walkStatus)
        }
    }

    fun getDogLoversInLocations(mapMarkerIds: List<UUID>, dogLoverRelationships: List<DogLoverRelationship>): Map<UUID, List<DogLoverInLocationDTO>> {
        val walks = walkRepository.findAllByMapMarkerIdInAndWalkStatusInAndDogLoverIdIn(
                mapMarkerIds,
                listOf(ONGOING, ARRIVED_AT_DESTINATION),
                dogLoverRelationships.map { it.dogLoverRelationshipId.receiverDogLover.id })

        return walks.groupBy { it.mapMarker.id }.mapValues { mapMarkerWalks ->
            mapMarkerWalks.value.map { walk ->
                DogLoverInLocationDTO(
                        walk.dogLover,
                        walk.dogs.map { DogBasicInfoDTO(it) },
                        dogLoverRelationships.find { it.dogLoverRelationshipId.receiverDogLover == walk.dogLover }?.relationshipStatus,
                        walk.walkStatus)
            }
        }
    }

    fun getArrivedAtDestinationWalkByDogLoverId(dogLoverId: UUID): Walk {
        // TODO Create mechanism which ensures there is always at most one record with ARRIVED_AT_DESTINATION status
        return walkRepository.findFirstByDogLoverIdAndWalkStatusOrderByTimestampDesc(dogLoverId, ARRIVED_AT_DESTINATION)
                ?: throw ArrivedAtDestinationWalkNotFoundException()
    }

    private fun validateDogLoverIsNotAlreadyOnWalk(dogLoverId: UUID) {
        if (walkRepository.existsByDogLoverIdAndWalkStatus(dogLoverId, ONGOING))
            throw DogLoverAlreadyOnWalkException()
    }

    private fun getWalkByIdAndDogLoverId(walkId: UUID, dogLoverId: UUID): Walk {
        return walkRepository.findByIdAndDogLoverId(walkId, dogLoverId)
                ?: throw WalkNotFoundException()
    }

    private fun isWalkStatusPermissible(currentWalkStatus: WalkStatus, newWalkStatus: WalkStatus): Boolean =
            when (currentWalkStatus to newWalkStatus) {
                ONGOING to ARRIVED_AT_DESTINATION -> true
                ARRIVED_AT_DESTINATION to LEFT_DESTINATION -> true
                currentWalkStatus to CANCELED -> true
                else -> false
            }
}