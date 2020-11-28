package pl.poznan.put.dogloverservice.modules.dogloverlike

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverAlreadyLikedException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverLikeNotExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoversNotInTheSameLocationException
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkService
import java.util.*

@Service
class DogLoverLikeService(
        private val dogLoverLikeRepository: DogLoverLikeRepository,
        private val walkService: WalkService,
        private val dogLoverService: DogLoverService
) {
    fun addLike(giverDogLoverId: UUID, receiverDogLoverId: UUID) {
        val dogLoverLikeId = createDogLoverLikeId(giverDogLoverId, receiverDogLoverId)
        validateDogLoverNotAlreadyLiked(dogLoverLikeId)

       addDogLoverLike(dogLoverLikeId)
    }

    fun removeLike(giverDogLoverId: UUID, receiverDogLoverId: UUID) {
        val dogLoverLikeId = createDogLoverLikeId(giverDogLoverId, receiverDogLoverId)
        validateDogLoverLikeExists(dogLoverLikeId)

        removeDogLoverLike(dogLoverLikeId)
    }

    @Transactional
    protected fun addDogLoverLike(dogLoverLikeId: DogLoverLikeId): DogLover {
        return dogLoverService.updateDogLover(
                dogLoverLikeId.receiverDogLoverWalk.dogLover.addLike()
        )
                .also { dogLoverLikeRepository.save(DogLoverLike(dogLoverLikeId)) }
    }

    @Transactional
    protected fun removeDogLoverLike(dogLoverLikeId: DogLoverLikeId): DogLover {
        return dogLoverService.updateDogLover(
                dogLoverLikeId.receiverDogLoverWalk.dogLover.removeLike()
        )
                .also { dogLoverLikeRepository.deleteById(dogLoverLikeId) }
    }

    private fun createDogLoverLikeId(giverDogLoverId: UUID, receiverDogLoverId: UUID): DogLoverLikeId {
        val giverDogLoverWalk = walkService.getArrivedAtDestinationWalkByDogLoverId(giverDogLoverId)
        val receiverDogLoverWalk = walkService.getArrivedAtDestinationWalkByDogLoverId(receiverDogLoverId)
        // Potencjalny bład, w przypadku, gdy dany Dog Lover będzie miał >1 ARRIVED_AT_DESTINATION spacer
        // Może to wystąpić w teorii

        validateDogLoversInTheSameLocation(giverDogLoverWalk, receiverDogLoverWalk)

        return DogLoverLikeId(giverDogLoverWalk, receiverDogLoverWalk)
    }

    private fun validateDogLoverNotAlreadyLiked(dogLoverLikeId: DogLoverLikeId) {
        if (dogLoverLikeRepository.existsById(dogLoverLikeId))
            throw DogLoverAlreadyLikedException()
    }

    private fun validateDogLoverLikeExists(dogLoverLikeId: DogLoverLikeId) {
        if (!dogLoverLikeRepository.existsById(dogLoverLikeId))
            throw DogLoverLikeNotExistsException()
    }

    private fun validateDogLoversInTheSameLocation(giverDogLoverWalk: Walk, receiverDogLoverWalk: Walk) {
        if (giverDogLoverWalk.mapMarker.id != receiverDogLoverWalk.mapMarker.id)
            throw DogLoversNotInTheSameLocationException()
    }
}