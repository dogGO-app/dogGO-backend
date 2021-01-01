package pl.poznan.put.dogloverservice.modules.doglover.dto

import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.util.*

data class DogLoverProfileDTO(
        val id: UUID,
        val nickname: String,
        val firstName: String,
        val lastName: String,
        val age: Int? = null,
        val hobby: String? = null,
        val likesCount: Int,
        val avatarChecksum: String?
) {
    constructor(dogLover: DogLover) : this(
            id = dogLover.id,
            nickname = dogLover.nickname,
            firstName = dogLover.firstName,
            lastName = dogLover.lastName,
            likesCount = dogLover.likesCount,
            avatarChecksum = dogLover.avatar?.checksum
    )
}