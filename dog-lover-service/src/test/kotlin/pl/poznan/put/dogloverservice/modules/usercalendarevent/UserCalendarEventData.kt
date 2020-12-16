package pl.poznan.put.dogloverservice.modules.usercalendarevent

import java.time.Instant
import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData

object UserCalendarEventData {

    val johnAndBurekEvent
        get() = UserCalendarEvent(
                id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
                dateTime = Instant.now().minusSeconds(10000),
                description = "Some description",
                dog = DogData.burek,
                dogLover = DogLoverData.john
        )

    val johnAndAzorEvent
        get() = UserCalendarEvent(
                id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
                dateTime = Instant.now().plusSeconds(10000),
                description = "Another description",
                dog = DogData.azor,
                dogLover = DogLoverData.john
        )

    val updatedJohnAndAzorEvent
        get() = UserCalendarEvent(
                id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
                dateTime = Instant.now().plusSeconds(100000),
                description = "Changed description",
                dog = DogData.azor,
                dogLover = DogLoverData.john
        )

    val johnEvents = listOf(
            johnAndBurekEvent, johnAndAzorEvent
    )

    fun getDogLoverEvent(dogLover: DogLover, dog: Dog) = UserCalendarEvent(
            id = UUID.fromString("a2d65908-e4ea-4388-bdd2-a1aef23431aa"),
            dateTime = Instant.now(),
            description = "Some description",
            dog = dog,
            dogLover = dogLover
    )
}