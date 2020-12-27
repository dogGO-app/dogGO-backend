package pl.poznan.put.dogloverservice.modules.dog

import java.time.LocalDate
import java.util.UUID
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData

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

    val azor
        get() = Dog(
                id = UUID.fromString("4995b41b-c78b-40dd-855f-602148aaa875"),
                name = "Azor",
                breed = "Shiba",
                color = "Brown",
                description = "Some other description",
                lastVaccinationDate = LocalDate.of(2020, 11, 18),
                dogLover = DogLoverData.john
        )

    val reksio
        get() = Dog(
                id = UUID.fromString("0747c0f8-e56a-4713-a110-da32ef4e39a6"),
                name = "Reksio",
                breed = "Shiba",
                color = "Brown",
                description = "Some description",
                lastVaccinationDate = LocalDate.of(2020, 5, 11),
                dogLover = DogLoverData.andrew
        )

    val bolt
        get() = Dog(
                id = UUID.fromString("0ac772ce-b542-441d-b37d-506bab63a132"),
                name = "Bolt",
                breed = "White German Shepherd ",
                color = "White",
                description = "Really fast",
                lastVaccinationDate = LocalDate.of(2020, 6, 13),
                dogLover = DogLoverData.tom
        )

    val yogi
        get() = Dog(
                id = UUID.fromString("9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5"),
                name = "yogi",
                breed = "york",
                color = "blue",
                description = "hauhau",
                lastVaccinationDate = LocalDate.of(2020, 12, 22),
                dogLover = DogLoverData.adam
        )
}