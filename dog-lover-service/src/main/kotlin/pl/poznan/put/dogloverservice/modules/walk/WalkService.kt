package pl.poznan.put.dogloverservice.modules.walk

import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverAlreadyOnWalkException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.WalkUpdateException
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO

@Service
class WalkService(
        private val walkRepository: WalkRepository,
        private val dogLoverService: DogLoverService,
        private val dogService: DogService,
        private val mapMarkerService: MapMarkerService
) {

    fun saveWalk(walkDTO: WalkDTO, dogLoverId: UUID): WalkDTO {
        checkIfDogLoverIsNotOnWalkAlready(dogLoverId)
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dog = dogService.getDogEntity(walkDTO.dogName, dogLoverId)
        val mapMarker = mapMarkerService.getMapMarker(walkDTO.mapMarker)
        val walk = Walk(
                UUID.randomUUID(),
                Instant.now(),
                dogLover,
                dog,
                mapMarker,
                WalkStatus.ONGOING
        )
        return WalkDTO(walkRepository.save(walk))
    }

    fun updateWalkStatus(walkId: UUID, walkStatus: WalkStatus, dogLoverId: UUID): WalkDTO {
        val walk = getWalkByIdAndDogLoverId(walkId, dogLoverId)
        if (!checkIfNewWalkStatusIsPermissible(Pair(walk.walkStatus, walkStatus)))
            throw WalkUpdateException()

        return WalkDTO(walkRepository.save(
                Walk(walk, walkStatus)))
    }

    private fun checkIfDogLoverIsNotOnWalkAlready(dogLoverId: UUID) {
        if (walkRepository.existsByDogLoverIdAndWalkStatus(dogLoverId, WalkStatus.ONGOING))
            throw DogLoverAlreadyOnWalkException()
    }

    private fun getWalkByIdAndDogLoverId(walkId: UUID, dogLoverId: UUID): Walk {
        return walkRepository.findByIdAndDogLoverId(walkId, dogLoverId)
                ?: throw WalkNotFoundException()
    }

    private fun checkIfNewWalkStatusIsPermissible(pair: Pair<WalkStatus, WalkStatus>): Boolean {
        return when (pair) {
            Pair(WalkStatus.ONGOING, WalkStatus.ARRIVED_AT_DESTINATION) -> true
            Pair(WalkStatus.ARRIVED_AT_DESTINATION, WalkStatus.LEFT_DESTINATION) -> true
            Pair(pair.first, WalkStatus.CANCELED) -> true
            else -> false
        }
    }
}