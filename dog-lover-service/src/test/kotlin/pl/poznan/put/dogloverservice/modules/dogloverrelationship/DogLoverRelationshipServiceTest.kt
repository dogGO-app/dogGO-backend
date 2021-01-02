package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto.DogLoverRelationshipDTO

class DogLoverRelationshipServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val dogLoverRelationshipRepository = mockk<DogLoverRelationshipRepository>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)
    val dogService = mockk<DogService>(relaxed = true)

    val dogLoverRelationshipService = DogLoverRelationshipService(
            dogLoverRelationshipRepository,
            dogLoverService,
            dogService
    )

    Given("dog lover id and receiver nickname") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val receiverNickname = "an.smith"

        every {
            dogLoverService.getDogLover(receiverNickname)
        } returns DogLoverData.andrew
        every {
            dogLoverService.getDogLover(dogLoverId)
        } returns dogLover
        every {
            dogLoverRelationshipRepository.findByIdOrNull(any())
        } returns null
        every {
            dogLoverRelationshipRepository.save(any())
        } returns DogLoverRelationshipData.johnWithAndrew
        every {
            dogLoverRelationshipRepository.existsById(any())
        } returns true

        When("adding new dog lover relationship") {
            dogLoverRelationshipService.addDogLoverRelationship(dogLoverId, receiverNickname, RelationshipStatus.FOLLOWS)

            Then("DogLoverRelationshipRepository::save should be called") {
                verify(exactly = 1) {
                    dogLoverRelationshipRepository.save(any())
                }
            }
        }

        When("removing dog lover relationship") {
            dogLoverRelationshipService.removeDogLoverRelationship(dogLoverId, receiverNickname)

            Then("DogLoverRelationshipRepository::deleteById should be called") {
                verify(exactly = 1) {
                    dogLoverRelationshipRepository.deleteById(any())
                }
            }
        }
    }

    Given("dog lover id") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val expectedDogLoverRelationships = DogLoverRelationshipData.johnRelationships
        val expectedDogLoverRelationshipsInfo = expectedDogLoverRelationships.map {
            DogLoverRelationshipDTO(it,
                    dogService.getDogs(it.id.receiverDogLover.id))
        }

        every {
            dogLoverRelationshipRepository.findAllByIdGiverDogLoverId(dogLoverId)
        } returns expectedDogLoverRelationships

        When("getting all dog lover relationships") {
            val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)

            Then("returned list should be equal to expected") {
                dogLoverRelationships shouldBe expectedDogLoverRelationships
            }
        }

        When("getting dog lover relationships info") {
            val dogLoverRelationshipsInfo = dogLoverRelationshipService.getDogLoverRelationshipsInfo(dogLoverId)

            Then("returned list should be equal to expected") {
                dogLoverRelationshipsInfo shouldBe expectedDogLoverRelationshipsInfo
            }
        }
    }
})