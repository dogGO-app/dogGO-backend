package pl.poznan.put.dogloverservice.modules.doglover.dto

import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class DogLoverProfileDTO(
        val nickname: String,
        val firstName: String,
        val lastName: String,
        val age: Int,
        val hobby: String?,
        val likesCount: Int
) {
    constructor(dogLover: DogLover) : this(
            nickname = dogLover.nickname,
            firstName = dogLover.firstName,
            lastName = dogLover.lastName,
            age = dogLover.age,
            hobby = dogLover.hobby,
            likesCount = dogLover.likesCount
    )
}