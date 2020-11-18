package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus

data class WalkDTO(

        val id: UUID? = null,

        val dogName: String,

        val mapMarker: UUID,

        val walkStatus: WalkStatus?

) {

    constructor(walk: Walk) : this(
            id = walk.id,
            dogName = walk.dog.name,
            mapMarker = walk.mapMarker.id,
            walkStatus = walk.walkStatus
    )
}