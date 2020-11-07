package pl.poznan.put.dogloverservice.modules.mapmarker

import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.Type
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.DecimalMax
import javax.validation.constraints.DecimalMin
import javax.validation.constraints.NotBlank

@Entity
class MapMarker(

        @Id
        @Type(type = "pg-uuid")
        val id: UUID,

        @field:NotBlank
        val name: String,

        val description: String?,

        @field:DecimalMin("-90.0")
        @field:DecimalMax("90.0")
        val latitude: Double,

        @field:DecimalMin("-180.0")
        @field:DecimalMax("180.0")
        val longitude: Double,

        val creationDate: Instant
)