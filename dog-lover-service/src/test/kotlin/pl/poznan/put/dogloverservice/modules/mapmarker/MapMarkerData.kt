package pl.poznan.put.dogloverservice.modules.mapmarker

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

object MapMarkerData {
    val parkMostowa
        get() = MapMarker(
                id = UUID.fromString("c3bd0f81-4f06-4d37-a039-251d41c6204d"),
                name = "Park Mostowa",
                description = "Nice place",
                latitude = 52.4,
                longitude = 16.9,
                creationDate = LocalDateTime.of(2019, 10, 5, 15, 23, 11)
                        .toInstant(ZoneOffset.of("+02:00"))
        )

    val parkKonin
        get() = MapMarker(
                id = UUID.fromString("87e5efcc-f8b4-48d4-9986-d2997b8cfda5"),
                name = "Park Konin",
                description = "Not so nice place",
                latitude = 52.22,
                longitude = 18.25,
                creationDate = LocalDateTime.of(2020, 5, 1, 15, 23, 11)
                        .toInstant(ZoneOffset.of("+02:00"))
        )

    val parks
        get() = listOf(parkMostowa, parkKonin)
}