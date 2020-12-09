package pl.poznan.put.dogloverservice.modules.walk.dto

import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class WalkDTO(

        val id: UUID? = null,

        val createdAt: LocalDateTime,

        val dogNames: List<String>,

        val mapMarker: UUID,

        val walkStatus: WalkStatus?

) {

    constructor(walk: Walk, timeZone: String) : this(
            id = walk.id,
            createdAt = walk.createdAt.atZone(ZoneId.of(timeZone)).toLocalDateTime(),
            dogNames = walk.dogs.map { it.name },
            mapMarker = walk.mapMarker.id,
            walkStatus = walk.walkStatus
    )
}