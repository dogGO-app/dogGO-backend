package pl.poznan.put.dogloverservice.modules.walk

import org.hibernate.annotations.Type
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarker
import java.time.Instant
import java.util.*
import javax.persistence.*

@Entity
class Walk(

        @Id
        @Type(type = "pg-uuid")
        val id: UUID = UUID.randomUUID(),

        val createdAt: Instant,

        @ManyToOne
        val dogLover: DogLover,

        @ManyToMany
        val dogs: List<Dog>,

        @ManyToOne
        val mapMarker: MapMarker,

        @Enumerated(EnumType.STRING)
        val walkStatus: WalkStatus
) {

    constructor(walk: Walk, walkStatus: WalkStatus) : this(
            id = walk.id,
            createdAt = walk.createdAt,
            dogLover = walk.dogLover,
            dogs = walk.dogs,
            mapMarker = walk.mapMarker,
            walkStatus = walkStatus
    )
}