package pl.poznan.put.dogloverservice.modules.walk

import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.Type
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarker
import javax.persistence.*

@Entity
class Walk(

        @Id
        @Type(type = "pg-uuid")
        val id: UUID = UUID.randomUUID(),

        val timestamp: Instant,

        @ManyToOne
        val dogLover: DogLover,

        @ManyToOne
        val dog: Dog,

        @ManyToOne
        val mapMarker: MapMarker,

        @Enumerated(EnumType.STRING)
        val walkStatus: WalkStatus
) {

    constructor(walk: Walk, walkStatus: WalkStatus) : this(
            walk.id,
            walk.timestamp,
            walk.dogLover,
            walk.dog,
            walk.mapMarker,
            walkStatus
    )
}