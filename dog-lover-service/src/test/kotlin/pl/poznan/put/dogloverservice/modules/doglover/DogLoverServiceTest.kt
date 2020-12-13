package pl.poznan.put.dogloverservice.modules.doglover

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNicknameAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNotFoundException
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import pl.poznan.put.dogloverservice.modules.doglover.dto.UpdateDogLoverProfileDTO
import java.util.*

class DogLoverServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val dogLoverRepository = mockk<DogLoverRepository>(relaxed = true)

    val dogLoverService = DogLoverService(dogLoverRepository)

    Given("existing dog lover id") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id

        every {
            dogLoverRepository.findByIdOrNull(dogLoverId)
        } returns dogLover

        And("expected dog lover profile DTO") {
            val dogLoverProfileDTO = DogLoverProfileDTO(dogLover)

            When("getting dog lover profile") {
                val actualDogLoverProfileDTO = dogLoverService.getDogLoverProfile(dogLoverId)

                Then("actual dog lover profile DTO should be equal to expected") {
                    actualDogLoverProfileDTO shouldBe dogLoverProfileDTO
                }
            }
        }

        And("update dog lover profile DTO") {
            val updatedDogLover = DogLoverData.updatedJohn
            val updateDogLoverProfileDTO = updatedDogLover.toUpdateDogLoverProfileDTO()
            val updatedDogLoverProfileDTO = DogLoverProfileDTO(updatedDogLover)

            every {
                dogLoverRepository.save(any())
            } returns updatedDogLover

            When("updating dog lover profile") {
                val actualUpdatedDogLoverProfileDTO = dogLoverService.updateDogLoverProfile(
                        updateDogLoverProfileDTO,
                        dogLoverId
                )

                Then("actual updated dog lover profile DTO should be equal to expected") {
                    actualUpdatedDogLoverProfileDTO shouldBe updatedDogLoverProfileDTO
                }
            }
        }
    }

    Given("non-existing dog lover id") {
        val dogLoverId = UUID.fromString("534e81a5-bdce-4de2-9344-c7a87706797d")

        every {
            dogLoverRepository.findByIdOrNull(dogLoverId)
        } returns null

        When("getting dog lover profile") {
            val exception = shouldThrow<DogLoverNotFoundException> {
                dogLoverService.getDogLoverProfile(dogLoverId)
            }

            Then("exception should have correct message") {
                exception shouldHaveMessage DogLoverNotFoundException().message
            }
        }

        When("getting dog lover entity") {
            val exception = shouldThrow<DogLoverNotFoundException> {
                dogLoverService.getDogLover(dogLoverId)
            }

            Then("exception should have correct message") {
                exception shouldHaveMessage DogLoverNotFoundException().message
            }
        }

        And("dog lover profile update DTO with non-existing nickname") {
            val dogLover = DogLoverData.updatedJohn
            val updateDogLoverProfileDTO = dogLover.toUpdateDogLoverProfileDTO()

            every {
                dogLoverRepository.existsByNickname(updateDogLoverProfileDTO.nickname)
            } returns false
            every {
                dogLoverRepository.save(any())
            } returns dogLover

            When("updating dog lover profile") {
                val actualDogLoverProfileDTO =
                        dogLoverService.updateDogLoverProfile(updateDogLoverProfileDTO, dogLoverId)

                Then("actual updated dog lover profile should be equal to expected") {
                    actualDogLoverProfileDTO shouldBe DogLoverProfileDTO(dogLover)
                }
            }
        }

        And("dog lover profile update DTO with already existing nickname") {
            val updateDogLoverProfileDTO = DogLoverData.john.toUpdateDogLoverProfileDTO()

            every {
                dogLoverRepository.existsByNickname(updateDogLoverProfileDTO.nickname)
            } returns true

            When("updating dog lover profile") {
                val exception = shouldThrow<DogLoverNicknameAlreadyExistsException> {
                    dogLoverService.updateDogLoverProfile(updateDogLoverProfileDTO, dogLoverId)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage DogLoverNicknameAlreadyExistsException().message
                }
            }
        }
    }

    Given("existing dog lover nickname") {
        val dogLover = DogLoverData.john
        val dogLoverNickname = dogLover.nickname

        every {
            dogLoverRepository.findByNickname(dogLoverNickname)
        } returns dogLover

        When("getting dog lover by nickname") {
            val actualDogLover = dogLoverService.getDogLover(dogLoverNickname)

            Then("actual dog lover should be equal to expected") {
                actualDogLover shouldBe dogLover
            }
        }
    }

    Given("non-existing dog lover nickname") {
        val dogLoverNickname = "some.non.existing.nickname"

        every {
            dogLoverRepository.findByNickname(dogLoverNickname)
        } returns null

        When("getting dog lover by nickname") {
            val exception = shouldThrow<DogLoverNotFoundException> {
                dogLoverService.getDogLover(dogLoverNickname)
            }

            Then("exception should have correct message") {
                exception shouldHaveMessage DogLoverNotFoundException().message
            }
        }
    }

    Given("dog lover entity") {
        val dogLover = DogLoverData.john

        And("dog lover exists") {
            every {
                dogLoverRepository.existsById(dogLover.id)
            } returns true
            every {
                dogLoverRepository.save(dogLover)
            } returns dogLover

            When("updating dog lover") {
                val actualUpdatedDogLover = dogLoverService.updateDogLover(dogLover)

                Then("actual updated dog lover entity should be equal to expected") {
                    actualUpdatedDogLover shouldBe dogLover
                }
            }
        }

        And("dog lover doesn't exist") {
            every {
                dogLoverRepository.existsById(dogLover.id)
            } returns false

            When("updating dog lover") {
                val exception = shouldThrow<DogLoverNotFoundException> {
                    dogLoverService.updateDogLover(dogLover)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage DogLoverNotFoundException().message
                }
            }
        }
    }
})

private fun DogLover.toUpdateDogLoverProfileDTO() = UpdateDogLoverProfileDTO(
        firstName = this.firstName,
        lastName = this.lastName,
        nickname = this.nickname,
        age = this.age,
        hobby = this.hobby
)