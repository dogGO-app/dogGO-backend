package pl.poznan.put.dogloverservice.modules.locationrecommendation.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarker
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

data class MapMarkerRecommendationDTO(

        val id: UUID,

        val name: String,

        val description: String?,

        val latitude: Double,

        val longitude: Double,

        val distance: Double
) {
    constructor(mapMarker: MapMarker, distance: Double) : this(
            id = mapMarker.id,
            name = mapMarker.name,
            description = mapMarker.description,
            latitude = mapMarker.latitude,
            longitude = mapMarker.longitude,
            distance = distance
    )
}