package pl.poznan.put.dogloverservice.modules.walk

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ActiveWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.ArrivedAtDestinationWalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkUpdateException
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.*
import pl.poznan.put.dogloverservice.modules.walk.dto.CreateWalkDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO
import java.time.Instant
import java.util.*

@Service
class WalkService(
        @Value("\${self.time-zone}") private val timeZone: String,
        private val walkRepository: WalkRepository,
        private val activeDogLoverWalkCache: ActiveDogLoverWalkCache,
        private val dogLoverService: DogLoverService,
        private val dogService: DogService,
        private val mapMarkerService: MapMarkerService,
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {
    fun getCompletedWalksHistory(dogLoverId: UUID): List<WalkDTO> {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        return walkRepository
                .findAllByDogLoverAndWalkStatusOrderByCreatedAtAsc(dogLover, walkStatus = LEFT_DESTINATION)
                .map { WalkDTO(it, timeZone) }
    }

    @Transactional
    fun saveWalk(createWalkDTO: CreateWalkDTO, dogLoverId: UUID): WalkDTO {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dogs = createWalkDTO.dogNames.map { dogService.getDog(it, dogLoverId) }
        val mapMarker = mapMarkerService.getMapMarker(createWalkDTO.mapMarker)
        val walk = Walk(
                createdAt = Instant.now(),
                dogLover = dogLover,
                dogs = dogs,
                mapMarker = mapMarker,
                walkStatus = ONGOING
        )

        walkRepository.completeDogLoverStartedWalks(dogLoverId)
        return WalkDTO(walk = walkRepository.save(walk), timeZone = timeZone)
                .also { activeDogLoverWalkCache.put(dogLoverId, it.id) }
    }

    fun updateWalkStatus(walkId: UUID, walkStatus: WalkStatus, dogLoverId: UUID): WalkDTO {
        val walk = getWalkByIdAndDogLoverId(walkId, dogLoverId)
        if (!isWalkStatusPermissible(currentWalkStatus = walk.walkStatus, newWalkStatus = walkStatus))
            throw WalkUpdateException()

        return WalkDTO(
                walk = walkRepository.save(Walk(walk, walkStatus)),
                timeZone = timeZone
        )
                .also {
                    if (walkStatus in WalkStatus.activeWalkStatuses()) {
                        activeDogLoverWalkCache.put(dogLoverId, it.id)
                    }
                }
    }

    fun keepWalkActive(dogLoverId: UUID) {
        val activeWalk = getActiveWalkByDogLoverId(dogLoverId)
        activeDogLoverWalkCache.put(dogLoverId, activeWalk.id)
    }

    // Change function name?
    fun getOtherDogLoversInLocation(mapMarkerId: UUID, dogLoverId: UUID): List<DogLoverInLocationDTO> {
        val otherDogLoversWalks = walkRepository.findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(
                mapMarkerId, ARRIVED_AT_DESTINATION, dogLoverId)
        val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)

        return otherDogLoversWalks.map { walk ->
            DogLoverInLocationDTO(walk,
                    dogLoverRelationships)
        }
    }

    // Change function name?
    fun getDogLoversInLocations(
            mapMarkerIds: List<UUID>,
            dogLoverRelationships: List<DogLoverRelationship>
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
        return walkRepository
                .findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId, setOf(ARRIVED_AT_DESTINATION))
                ?: throw ArrivedAtDestinationWalkNotFoundException()
    }

    private fun getActiveWalkByDogLoverId(dogLoverId: UUID): Walk {
        return walkRepository
                .findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(
                        dogLoverId,
                        WalkStatus.activeWalkStatuses()
                )
                ?: throw ActiveWalkNotFoundException()
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