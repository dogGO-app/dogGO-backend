package pl.poznan.put.dogloverservice.modules.doglover

import pl.poznan.put.dogloverservice.modules.doglover.dto.UpdateDogLoverProfileDTO
import java.util.*

object DogLoverData {
    val john
        get() = DogLover(
                id = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053"),
                firstName = "John",
                lastName = "Kowalski",
                nickname = "john.kowalski",
                likesCount = 1
        )

    val updatedJohn
        get() = DogLover(
                id = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053"),
                firstName = "John",
                lastName = "Nowak",
                nickname = "john.nowak"
        )

    val andrew
        get() = DogLover(
                id = UUID.fromString("2135274f-6b3d-46a0-a1a2-c4432c67d409"),
                firstName = "Andrew",
                lastName = "Smith",
                nickname = "an.smith"
        )

    val tom
        get() = DogLover(
                id = UUID.fromString("6895b8e9-76ed-40cd-9e53-cec61a9571b5"),
                firstName = "Tom",
                lastName = "Smith",
                nickname = "tom.smith"
        )

    val adam
        get() = DogLover(
                id = UUID.fromString("9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5"),
                firstName = "Adam",
                lastName = "Malysz",
                nickname = "adas.malysz11"
        )

    val updateAdamProfileDTO
        get() = UpdateDogLoverProfileDTO(
                firstName = "Adam",
                lastName = "Nowak",
                nickname = "adas.malysz11"
        )
}