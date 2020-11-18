package pl.poznan.put.dogloverservice.modules.walk

import java.time.Instant
import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
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
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dog = dogService.getDogEntity(walkDTO.dogName, dogLoverId)
        val mapMarker = mapMarkerService.getMapMarker(walkDTO.mapMarker)
        checkIfDogLoverIsNotOnWalkAlreadyOrThrow(dogLoverId)
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
        checkIfWalkBelongsToDogLoverOrThrow(walkId, dogLoverId)
        checkIfNewWalkStatusIsPermissibleOrThrow(walkId, walkStatus)

        return changeWalkStatus(walkId, walkStatus)
    }

    private fun checkIfDogLoverIsNotOnWalkAlreadyOrThrow(dogLoverId: UUID) {
        if (walkRepository.existsByDogLoverIdAndWalkStatus(dogLoverId, WalkStatus.ONGOING))
            throw DogLoverAlreadyOnWalkException()
    }

    private fun checkIfWalkBelongsToDogLoverOrThrow(walkId: UUID, dogLoverId: UUID) {
        if (!walkRepository.existsByIdAndDogLoverId(walkId, dogLoverId))
            throw WalkNotFoundException()
    }

    private fun checkIfNewWalkStatusIsPermissibleOrThrow(walkId: UUID, walkStatus: WalkStatus) {
        when (walkStatus) {
            WalkStatus.ONGOING -> throw WalkUpdateException("Cannot set ongoing walk status in existing walk.")
            WalkStatus.ARRIVED_AT_DESTINATION -> if (!walkRepository.existsByIdAndWalkStatus(walkId, WalkStatus.ONGOING))
                throw WalkUpdateException("Cannot set arrived at destination walk status - ongoing walk not exists.")
            WalkStatus.LEFT_DESTINATION -> if (!walkRepository.existsByIdAndWalkStatus(walkId, WalkStatus.ARRIVED_AT_DESTINATION))
                throw WalkUpdateException("Cannot set left destination walk status - arrived at destination walk not exists.")
            WalkStatus.CANCELED -> return
        }
    }

    private fun changeWalkStatus(walkId: UUID, walkStatus: WalkStatus): WalkDTO {
        val walk = walkRepository.findByIdOrNull(walkId) ?: throw WalkNotFoundException()
        return WalkDTO(walkRepository.save(
                Walk(walk, walkStatus)))
    }
}