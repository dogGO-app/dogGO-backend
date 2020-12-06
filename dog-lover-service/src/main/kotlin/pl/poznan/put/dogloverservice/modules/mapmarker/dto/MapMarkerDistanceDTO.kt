package pl.poznan.put.dogloverservice.modules.mapmarker.dto

import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerDistanceView
import java.util.*
import kotlin.math.roundToInt

data class MapMarkerDistanceDTO(

        val id: UUID,

        val name: String,

        val description: String?,

        val latitude: Double,

        val longitude: Double,

        val distanceInMeters: Int
) {

    constructor(mapMarkerDistance: MapMarkerDistanceView) : this(
            id = mapMarkerDistance.id,
            name = mapMarkerDistance.name,
            description = mapMarkerDistance.description,
            latitude = mapMarkerDistance.latitude,
            longitude = mapMarkerDistance.longitude,
            distanceInMeters = mapMarkerDistance.distanceInMeters.roundToInt()
    )
}