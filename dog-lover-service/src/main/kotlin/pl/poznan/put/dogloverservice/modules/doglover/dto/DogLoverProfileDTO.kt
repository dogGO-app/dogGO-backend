package pl.poznan.put.dogloverservice.modules.doglover.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class DogLoverProfileDTO(
        val id: UUID,
        val nickname: String,
        val firstName: String,
        val lastName: String,
        val age: Int? = null,
        val hobby: String? = null,
        val likesCount: Int
) {
    constructor(dogLover: DogLover) : this(
            id = dogLover.id,
            nickname = dogLover.nickname,
            firstName = dogLover.firstName,
            lastName = dogLover.lastName,
            likesCount = dogLover.likesCount
    )
}