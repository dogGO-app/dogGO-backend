package pl.poznan.put.dogloverservice.modules.mapmarker.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarker
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

data class MapMarkerDTO(
        val id: UUID,

        @field:NotBlank
        val name: String,

        val description: String?,

        @field:DecimalMin("-90.0")
        @field:DecimalMax("90.0")
        val latitude: Double,

        @field:DecimalMin("-180.0")
        @field:DecimalMax("180.0")
        val longitude: Double
) {
    constructor(mapMarker: MapMarker) : this(
            id = mapMarker.id,
            name = mapMarker.name,
            description = mapMarker.description,
            latitude = mapMarker.latitude,
            longitude = mapMarker.longitude
    )
}