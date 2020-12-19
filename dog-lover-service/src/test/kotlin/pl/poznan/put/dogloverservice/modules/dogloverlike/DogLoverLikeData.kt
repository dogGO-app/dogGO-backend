package pl.poznan.put.dogloverservice.modules.dogloverlike

import java.time.Instant
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.walk.WalkData.getArrivedAtDestination

object DogLoverLikeData {

    fun getDogLoversLike(giverDogLover: DogLover, receiverDogLover: DogLover) = DogLoverLike(
            id = DogLoverLikeId(
                    giverDogLoverWalk = getArrivedAtDestination(giverDogLover),
                    receiverDogLoverWalk = getArrivedAtDestination(receiverDogLover)
            ),
            createdAt = Instant.now()
    )
}