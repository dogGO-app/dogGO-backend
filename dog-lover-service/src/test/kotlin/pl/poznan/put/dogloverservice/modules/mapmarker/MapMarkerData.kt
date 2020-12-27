package pl.poznan.put.dogloverservice.modules.mapmarker

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDistanceDTO

object MapMarkerData {
    val parkMostowa
        get() = MapMarker(
                id = UUID.fromString("717e718d-d56f-4771-b720-b90688405eae"),
                name = "Park Mostowa",
                description = "Nice place",
                latitude = 52.4,
                longitude = 18.9,
                creationDate = LocalDateTime.of(2019, 10, 5, 15, 23, 11)
                        .toInstant(ZoneOffset.of("+02:00"))
        )

    val parkKonin
        get() = MapMarker(
                id = UUID.fromString("27ebd303-5474-4ba6-a278-7253334fcb20"),
                name = "Park Konin",
                description = "Not so nice place",
                latitude = 52.22,
                longitude = 18.25,
                creationDate = LocalDateTime.of(2020, 5, 1, 15, 23, 11)
                        .toInstant(ZoneOffset.of("+02:00"))
        )

    val parks
        get() = listOf(parkMostowa, parkKonin)

    val parksWithDistances
        get() = listOf(
                MapMarkerDistanceDTO(
                        id = UUID.fromString("c3bd0f81-4f06-4d37-a039-251d41c6204d"),
                        name = "Park Mostowa",
                        description = "Nice place",
                        latitude = 52.4,
                        longitude = 16.9,
                        distanceInMeters = 20),
                MapMarkerDistanceDTO(
                        id = UUID.fromString("87e5efcc-f8b4-48d4-9986-d2997b8cfda5"),
                        name = "Park Konin",
                        description = "Not so nice place",
                        latitude = 52.22,
                        longitude = 18.25,
                        distanceInMeters = 100)
        )

    val parksDistanceViews
        get() = listOf(
                createMapMarkerDistanceView(
                        id = UUID.fromString("c3bd0f81-4f06-4d37-a039-251d41c6204d"),
                        name = "Park Mostowa",
                        description = "Nice place",
                        latitude = 52.4,
                        longitude = 16.9,
                        distanceInMeters = 20.32
                ),
                createMapMarkerDistanceView(
                        id = UUID.fromString("87e5efcc-f8b4-48d4-9986-d2997b8cfda5"),
                        name = "Park Konin",
                        description = "Not so nice place",
                        latitude = 52.22,
                        longitude = 18.25,
                        distanceInMeters = 100.12
                )
        )

    fun createMapMarkerDistanceView(
            id: UUID,
            name: String,
            description: String?,
            latitude: Double,
            longitude: Double,
            distanceInMeters: Double
    ) = object : MapMarkerDistanceView {
        override val id: UUID
            get() = id
        override val name: String
            get() = name
        override val description: String?
            get() = description
        override val latitude: Double
            get() = latitude
        override val longitude: Double
            get() = longitude
        override val distanceInMeters: Double
            get() = distanceInMeters
    }
}