package pl.poznan.put.dogloverservice.modules.doglover

import java.util.*

object DogLoverStub {
    val dogLover
        get() = DogLover(
                id = UUID.fromString("11443bdb-a4c9-4921-8a14-239d10189053"),
                firstName = "Jan",
                lastName = "Kowalski",
                nickname = "jan.kowalski",
                age = 40,
                hobby = "Testowanie"
        )
}