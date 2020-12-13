package pl.poznan.put.dogloverservice.modules.dog

import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import java.time.LocalDate
import java.util.*

object DogData {
    val burek
        get() = Dog(
                id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
                name = "Burek",
                breed = "Shiba",
                color = "Yellow",
                description = "Some description",
                lastVaccinationDate = LocalDate.of(2020, 11, 18),
                dogLover = DogLoverData.john
        )

    val updatedBurek
        get() = Dog(
                id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
                name = "Burek",
                breed = "Shiba",
                color = "Black",
                description = "Is very polite",
                lastVaccinationDate = LocalDate.of(2020, 12, 15),
                dogLover = DogLoverData.john
        )
}