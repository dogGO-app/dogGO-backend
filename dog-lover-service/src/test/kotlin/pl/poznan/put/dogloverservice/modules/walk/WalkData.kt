package pl.poznan.put.dogloverservice.modules.walk

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerData

object WalkData {
    val historicalWalks
        get() = listOf(
                Walk(
                        id = UUID.fromString("9fd07ae4-0037-4a3b-a767-1b0fbd243942"),
                        createdAt = LocalDateTime.of(2020, 10, 5, 16, 23, 11)
                                .toInstant(ZoneOffset.of("+02:00")),
                        dogLover = DogLoverData.john,
                        dogs = listOf(DogData.burek, DogData.azor),
                        mapMarker = MapMarkerData.parkMostowa,
                        walkStatus = WalkStatus.LEFT_DESTINATION
                ),
                Walk(
                        id = UUID.fromString("7881eb4a-72c8-4286-8505-0cfa431833e1"),
                        createdAt = LocalDateTime.of(2020, 9, 5, 16, 23, 11)
                                .toInstant(ZoneOffset.of("+02:00")),
                        dogLover = DogLoverData.john,
                        dogs = listOf(DogData.azor),
                        mapMarker = MapMarkerData.parkKonin,
                        walkStatus = WalkStatus.LEFT_DESTINATION
                )
        )

    val dogLoversArrivedAtDestinationWalks
        get() = listOf(
                Walk(
                        id = UUID.fromString("2ae70aca-09d9-4697-8c10-c0ca1d00b83c"),
                        createdAt = LocalDateTime.of(2020, 10, 5, 16, 23, 11)
                                .toInstant(ZoneOffset.of("+02:00")),
                        dogLover = DogLoverData.andrew,
                        dogs = listOf(DogData.reksio),
                        mapMarker = MapMarkerData.parkMostowa,
                        walkStatus = WalkStatus.ARRIVED_AT_DESTINATION
                ),
                Walk(
                        id = UUID.fromString("0747c0f8-e56a-4713-a110-da32ef4e39a6"),
                        createdAt = LocalDateTime.of(2020, 9, 5, 16, 12, 11)
                                .toInstant(ZoneOffset.of("+02:00")),
                        dogLover = DogLoverData.tom,
                        dogs = listOf(DogData.bolt),
                        mapMarker = MapMarkerData.parkKonin,
                        walkStatus = WalkStatus.ARRIVED_AT_DESTINATION
                )
        )

    val ongoing
        get() = Walk(
                id = UUID.fromString("91dc3a5b-3970-4ee8-adc2-bc073d5d70b4"),
                createdAt = LocalDateTime.now().toInstant(ZoneOffset.of("+02:00")),
                dogLover = DogLoverData.john,
                dogs = listOf(DogData.azor),
                mapMarker = MapMarkerData.parkKonin,
                walkStatus = WalkStatus.ONGOING
        )

    fun getArrivedAtDestination(dogLover: DogLover) = Walk(
            id = UUID.fromString("abbc3e50-21b3-4462-8b8b-b21842ec7ca5"),
            createdAt = LocalDateTime.now().toInstant(ZoneOffset.of("+02:00")),
            dogLover = dogLover,
            dogs = listOf(DogData.reksio),
            mapMarker = MapMarkerData.parkMostowa,
            walkStatus = WalkStatus.ARRIVED_AT_DESTINATION
    )
}