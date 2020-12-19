package pl.poznan.put.dogloverservice.modules.dogloverlike

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverlike.DogLoverLikeData.getDogLoversLike
import pl.poznan.put.dogloverservice.modules.walk.WalkData.getArrivedAtDestination
import pl.poznan.put.dogloverservice.modules.walk.WalkService

class DogLoverLikeServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val dogLoverLikeRepository = mockk<DogLoverLikeRepository>(relaxed = true)
    val walkService = mockk<WalkService>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)

    val dogLoverLikeService = DogLoverLikeService(
            dogLoverLikeRepository,
            walkService,
            dogLoverService
    )

    Given("giver dog lover id and receiver dog lover id") {
        val giverDogLover = DogLoverData.john
        val giverDogLoverId = giverDogLover.id
        val giverDogLoverWalk = getArrivedAtDestination(giverDogLover)
        val receiverDogLover = DogLoverData.andrew
        val receiverDogLoverId = receiverDogLover.id
        val receiverDogLoverWalk = getArrivedAtDestination(receiverDogLover)
        val johnToAndrewLike = getDogLoversLike(giverDogLover, receiverDogLover)
        receiverDogLover.addLike()

        every {
            walkService.getArrivedAtDestinationWalkByDogLoverId(giverDogLoverId)
        } returns giverDogLoverWalk
        every {
            walkService.getArrivedAtDestinationWalkByDogLoverId(receiverDogLoverId)
        } returns receiverDogLoverWalk
        every {
            walkService.getArrivedAtDestinationWalkByDogLoverId(giverDogLoverId)
        } returns giverDogLoverWalk
        every {
            walkService.getArrivedAtDestinationWalkByDogLoverId(receiverDogLoverId)
        } returns receiverDogLoverWalk
        every {
            dogLoverService.updateDogLover(any())
        } returns receiverDogLover
        every {
            dogLoverLikeRepository.save(any())
        } returns johnToAndrewLike

        When("adding dog lover like") {
            dogLoverLikeService.addLike(giverDogLoverId, receiverDogLoverId)

            Then("DogLoverLikeRepository::save and DogLoverService::updateDogLover should be called") {
                verify(exactly = 1) {
                    dogLoverLikeRepository.save(any())
                    dogLoverService.updateDogLover(any())
                }
            }
        }

        every {
            dogLoverLikeRepository.existsById(any())
        } returns true

        When("removing dog lover like") {
            dogLoverLikeService.removeLike(giverDogLoverId, receiverDogLoverId)

            Then("DogLoverLikeRepository::save and DogLoverService::deleteById should be called") {
                verify(exactly = 1) {
                    dogLoverLikeRepository.deleteById(any())
                    dogLoverService.updateDogLover(any())
                }
            }
        }
    }
})