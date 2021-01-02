package pl.poznan.put.dogloverservice.modules.walk.dto

import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

data class WalkDTO(

        val id: UUID,

        val createdAt: LocalDateTime,

        val dogs: List<DogDTO>,

        val mapMarker: MapMarkerDTO,

        val walkStatus: WalkStatus

) {

    constructor(walk: Walk, timeZone: String) : this(
            id = walk.id,
            createdAt = walk.createdAt.atZone(ZoneId.of(timeZone)).toLocalDateTime(),
            dogs = walk.dogs.map { DogDTO(it) },
            mapMarker = MapMarkerDTO(walk.mapMarker),
            walkStatus = walk.walkStatus
    )
}