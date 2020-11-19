package pl.poznan.put.dogloverservice.modules.dog

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverStub
import java.time.LocalDate
import java.util.*

class DogServiceTest : BehaviorSpec({
    val dogRepository = mockk<DogRepository>(relaxed = true)
    val dogLoverService = mockk<DogLoverService>(relaxed = true)

    val dogService = DogService(
            dogRepository,
            dogLoverService
    )

    Given("dog name and dog lover id") {
        val dogName = "Burek"
        val dogLoverId = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053")

        every {
            dogRepository.findByNameAndDogLoverId(dogName, dogLoverId)
        } returns Dog(
                name = "Burek",
                breed = null,
                color = "Różowy",
                description = "Jakiś opis",
                lastVaccinationDate = LocalDate.of(2020, 11, 18),
                dogLover = DogLoverStub.dogLover
        )

                When("getting dog") {
                    val dogDTO = dogService.getDog(dogName, dogLoverId)

                    Then("actual dogDTO should be equal to expected") {
                        dogDTO shouldBe DogDTO(
                                name = "Burek",
                                breed = null,
                                color = "Różowy",
                                description = "Jakiś opis",
                                lastVaccinationDate = LocalDate.of(2020, 11, 18)
                        )
                    }
                }
    }
})