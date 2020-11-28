package pl.poznan.put.dogloverservice.modules.dogloverlike.dto

import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.util.*

data class DogLoverLikesCountDTO(
        val dogLoverId: UUID,
        val likesCount: Int
) {
    constructor(dogLover: DogLover) : this(
            dogLoverId = dogLover.id,
            likesCount = dogLover.likesCount
    )
}