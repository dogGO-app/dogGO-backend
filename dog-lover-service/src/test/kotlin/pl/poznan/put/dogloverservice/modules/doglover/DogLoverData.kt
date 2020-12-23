package pl.poznan.put.dogloverservice.modules.doglover

import java.util.*
import pl.poznan.put.dogloverservice.modules.doglover.dto.UpdateDogLoverProfileDTO

object DogLoverData {
    val john
        get() = DogLover(
                id = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053"),
                firstName = "John",
                lastName = "Kowalski",
                nickname = "john.kowalski",
                age = 40,
                hobby = "Testing"
        )

    val updatedJohn
        get() = DogLover(
                id = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053"),
                firstName = "John",
                lastName = "Nowak",
                nickname = "john.nowak",
                age = 40,
                hobby = "Testing and riding bike"
        )

    val andrew
        get() = DogLover(
                id = UUID.fromString("2135274f-6b3d-46a0-a1a2-c4432c67d409"),
                firstName = "Andrew",
                lastName = "Smith",
                nickname = "an.smith",
                age = 33,
                hobby = "Hiking"
        )

    val tom
        get() = DogLover(
                id = UUID.fromString("6895b8e9-76ed-40cd-9e53-cec61a9571b5"),
                firstName = "Tom",
                lastName = "Smith",
                nickname = "tom.smith",
                age = 31,
                hobby = "Skiing"
        )

    fun getUpdateJohnProfileDTO(nickname: String) =
            UpdateDogLoverProfileDTO(
                firstName = "John",
                lastName = "Kowalski",
                nickname = nickname,
                age = 40,
                hobby = "Testing"
        )
}