package pl.poznan.put.dogloverservice.modules.walk

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ArrivedAtDestinationWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverAlreadyOnWalkException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkUpdateException
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.*
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO
import java.time.Instant
import java.util.*

@Service
class WalkService(
        @Value("\${self.time-zone}") private val timeZone: String,
        private val walkRepository: WalkRepository,
        private val dogLoverService: DogLoverService,
        private val dogService: DogService,
        private val mapMarkerService: MapMarkerService,
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {
    fun getWalks(dogLoverId: UUID): List<WalkDTO> {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        return walkRepository.findAllByDogLoverOrderByCreatedAtAsc(dogLover)
                .map { WalkDTO(it, timeZone) }
    }

    fun saveWalk(walkDTO: WalkDTO, dogLoverId: UUID): WalkDTO {
        validateDogLoverIsNotAlreadyOnWalk(dogLoverId)
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dogs = walkDTO.dogNames.map { dogService.getDogEntity(it, dogLoverId) }
        val mapMarker = mapMarkerService.getMapMarker(walkDTO.mapMarker)
        val walk = Walk(
                createdAt = Instant.now(),
                dogLover = dogLover,
                dogs = dogs,
                mapMarker = mapMarker,
                walkStatus = ONGOING
        )
        return WalkDTO(walk = walkRepository.save(walk), timeZone = timeZone)
    }

    fun updateWalkStatus(walkId: UUID, walkStatus: WalkStatus, dogLoverId: UUID): WalkDTO {
        val walk = getWalkByIdAndDogLoverId(walkId, dogLoverId)
        if (!isWalkStatusPermissible(currentWalkStatus = walk.walkStatus, newWalkStatus = walkStatus))
            throw WalkUpdateException()

        return WalkDTO(
                walk = walkRepository.save(Walk(walk, walkStatus)),
                timeZone = timeZone
        )
    }

    fun getOtherDogLoversInLocation(mapMarkerId: UUID, dogLoverId: UUID): List<DogLoverInLocationDTO> {
        val otherDogLoversWalks = walkRepository.findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(
                mapMarkerId, ARRIVED_AT_DESTINATION, dogLoverId)
        val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)

        return otherDogLoversWalks.map { walk ->
            DogLoverInLocationDTO(walk,
                    dogLoverRelationships)
        }
    }

    fun getDogLoversInLocations(mapMarkerIds: List<UUID>, dogLoverRelationships: List<DogLoverRelationship>
    ): Map<UUID, List<DogLoverInLocationDTO>> {
        val walks = walkRepository.findAllByMapMarkerIdInAndWalkStatusInAndDogLoverIdIn(
                mapMarkerIds,
                listOf(ONGOING, ARRIVED_AT_DESTINATION),
                dogLoverRelationships.map { it.id.receiverDogLover.id })

        return walks
                .groupBy { it.mapMarker.id }
                .mapValues { (_, walks) ->
                    walks.map { walk ->
                        DogLoverInLocationDTO(
                                walk,
                                dogLoverRelationships
                        )
                    }
                }
    }

    fun getArrivedAtDestinationWalkByDogLoverId(dogLoverId: UUID): Walk {
        // TODO Create mechanism which ensures there is always at most one record with ARRIVED_AT_DESTINATION status
        return walkRepository.findFirstByDogLoverIdAndWalkStatusOrderByCreatedAtDesc(dogLoverId, ARRIVED_AT_DESTINATION)
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
                ONGOING to CANCELED -> true
                ARRIVED_AT_DESTINATION to LEFT_DESTINATION -> true
                else -> false
            }
}