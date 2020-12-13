package pl.poznan.put.dogloverservice.modules.doglover

import java.util.*

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
}