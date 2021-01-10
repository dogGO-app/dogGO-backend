package pl.poznan.put.dogloverservice.modules.dog

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogNotFoundException
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.dog.dto.UpdateDogDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEventRepository

class DogServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val dogRepository = mockk<DogRepository>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)
    val calendarEventRepository = mockk<UserCalendarEventRepository>(relaxed = true)

    val dogService = DogService(
            dogRepository,
            dogLoverService,
            calendarEventRepository
    )

    Given("existing dog name and dog lover id") {
        val dogName = "Burek"
        val dogLover = DogLoverData.john
        val dog = DogData.burek

        every {
            dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(dogName, dogLover.id)
        } returns dog

        When("getting dog entity") {
            val actualDog = dogService.getDog(dogName, dogLover.id)

            Then("actual dogDTO should be equal to expected") {
                actualDog shouldBe dog
            }
        }
    }

    Given("non-existing dog name and dog lover id") {
        val dogName = "Janusz"
        val dogLover = DogLoverData.john

        every {
            dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(dogName, dogLover.id)
        } returns null

        When("getting dog entity") {
            val exception = shouldThrow<DogNotFoundException> {
                dogService.getDog(dogName, dogLover.id)
            }

            Then("DogNotFoundException should have correct message") {
                exception.message shouldBe DogNotFoundException(dogName, dogLover.id).message
            }
        }
    }

    Given("DogDTO with non-existing dog name and dog lover id") {
        val dog = DogData.burek
        val dogDTO = UpdateDogDTO(dog)
        val expectedDogDTO = DogDTO(dog)
        val dogLoverId = DogLoverData.john.id

        every {
            dogRepository.existsByNameAndDogLoverIdAndRemovedIsFalse(dogDTO.name, dogLoverId)
        } returns false
        every {
            dogRepository.save(any())
        } returns dog
        every {
            dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(dogDTO.name, dogLoverId)
        } returns null

        When("adding new dog") {
            val actualDogDTO = dogService.addDog(dogDTO, dogLoverId)

            Then("actual DogDTO should be equal to expected") {
                actualDogDTO shouldBe expectedDogDTO
            }

            Then("DogRepository::save should be called") {
                verify(exactly = 1) {
                    dogRepository.save(any())
                }
            }
        }

        When("updating dog") {
            val exception = shouldThrow<DogNotFoundException> {
                dogService.updateDog(dogDTO, dogLoverId)
            }

            Then("DogNotFoundException should have correct message") {
                exception.message shouldBe DogNotFoundException(dogDTO.name, dogLoverId).message
            }
        }
    }

    Given("DogDTO with existing dog name and dog lover id") {
        val dog = DogData.burek
        val updatedDog = DogData.updatedBurek
        val updatedDogDTO = UpdateDogDTO(updatedDog)
        val expectedDogDTO = DogDTO(updatedDog)
        val dogLoverId = DogLoverData.john.id

        every {
            dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(updatedDogDTO.name, dogLoverId)
        } returns dog
        every {
            dogRepository.save(any())
        } returns updatedDog

        When("updating dog") {
            val actualDogDTO = dogService.updateDog(updatedDogDTO, dogLoverId)

            Then("actual DogDTO should be equal to expected") {
                actualDogDTO shouldBe expectedDogDTO
            }
        }
    }

    Given("Dog lover id and dog id") {
        val dogId = DogData.burek.id
        val dogLoverId = DogLoverData.john.id

        every {
            dogRepository.existsByIdAndDogLoverIdAndRemovedIsFalse(dogId, dogLoverId)
        } returns true
        every {
            dogRepository.countAllByDogLoverIdAndRemovedIsFalse(dogLoverId)
        } returns 2

        When("removing dog") {
            dogService.removeDog(dogId, dogLoverId)

            Then("dogRepository::setRemovedToTrue should be called") {
                verify(exactly = 1) {
                    dogRepository.setRemovedToTrue(dogId, dogLoverId)
                }
            }

            Then("calendarEventRepository::deleteAllByDogIdAndDogLoverIdAndDateTimeAfter should be called") {
                verify(exactly = 1) {
                    calendarEventRepository.deleteAllByDogIdAndDogLoverIdAndDateTimeAfter(dogId, dogLoverId, any())
                }
            }
        }
    }
})