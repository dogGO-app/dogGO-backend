package pl.poznan.put.dogloverservice.modules.dogloverlike

import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverAlreadyLikedException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverLikeNotExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoversNotInTheSameLocationException
import pl.poznan.put.dogloverservice.modules.walk.WalkService
import java.util.*

@Service
class DogLoverLikeService(
        private val dogLoverLikeRepository: DogLoverLikeRepository,
        private val walkService: WalkService
) {
    fun addLike(giverDogLoverId: UUID, receiverDogLoverId: UUID) {
        val dogLoverLikeId = createDogLoverLikeId(giverDogLoverId, receiverDogLoverId)
        checkDogLoverAlreadyLiked(dogLoverLikeId)

        dogLoverLikeRepository.save(DogLoverLike(dogLoverLikeId))
    }

    fun removeLike(giverDogLoverId: UUID, receiverDogLoverId: UUID) {
        val dogLoverLikeId = createDogLoverLikeId(giverDogLoverId, receiverDogLoverId)
        checkDogLoverLikeNotExists(dogLoverLikeId)

        dogLoverLikeRepository.deleteById(dogLoverLikeId)
    }

    private fun createDogLoverLikeId(giverDogLoverId: UUID, receiverDogLoverId: UUID): DogLoverLikeId {
        val giverDogLoverWalk = walkService.getArrivedAtDestinationWalkByDogLoverId(giverDogLoverId)
        val receiverDogLoverWalk = walkService.getArrivedAtDestinationWalkByDogLoverId(receiverDogLoverId)
        // Potencjalny bład, w przypadku, gdy dany Dog Lover będzie miał >1 ARRIVED_AT_DESTINATION spacer
        // Może to wystąpić w teorii

        if (giverDogLoverWalk.mapMarker.id != receiverDogLoverWalk.mapMarker.id)
            throw DogLoversNotInTheSameLocationException()

        return DogLoverLikeId(giverDogLoverWalk, receiverDogLoverWalk)
    }

    private fun checkDogLoverAlreadyLiked(dogLoverLikeId: DogLoverLikeId) {
        if (dogLoverLikeRepository.existsById(dogLoverLikeId))
            throw DogLoverAlreadyLikedException()
    }

    private fun checkDogLoverLikeNotExists(dogLoverLikeId: DogLoverLikeId) {
        if (!dogLoverLikeRepository.existsById(dogLoverLikeId))
            throw DogLoverLikeNotExistsException()
    }
}