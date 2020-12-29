package pl.poznan.put.dogloverservice.modules.walk.dto

import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO

data class WalkDTO(

        val id: UUID,

        val createdAt: LocalDateTime,

        val dogNames: List<String>,

        val mapMarker: MapMarkerDTO,

        val walkStatus: WalkStatus

) {

    constructor(walk: Walk, timeZone: String) : this(
            id = walk.id,
            createdAt = walk.createdAt.atZone(ZoneId.of(timeZone)).toLocalDateTime(),
            dogNames = walk.dogs.map { it.name },
            mapMarker = MapMarkerDTO(walk.mapMarker),
            walkStatus = walk.walkStatus
    )
}