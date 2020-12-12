package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.*

data class CreateWalkDTO(
        val dogNames: List<String>,
        val mapMarker: UUID
)