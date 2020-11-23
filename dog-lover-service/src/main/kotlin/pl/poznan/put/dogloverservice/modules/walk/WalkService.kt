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
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.ARRIVED_AT_DESTINATION
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.CANCELED
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.LEFT_DESTINATION
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus.ONGOING
import pl.poznan.put.dogloverservice.modules.walk.dto.DogInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.UserInLocationDTO
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

    fun getUsersInLocation(mapMarkerId: UUID, dogLoverId: UUID): List<UserInLocationDTO> {
        val walks = walkRepository.findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(mapMarkerId, ARRIVED_AT_DESTINATION, dogLoverId)

        return walks.map { walk ->
            UserInLocationDTO(
                    walk.dogLover,
                    walk.dogs.map { DogInLocationDTO(it) })
        }
    }

    private fun checkIfDogLoverIsNotOnWalkAlready(dogLoverId: UUID) {
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