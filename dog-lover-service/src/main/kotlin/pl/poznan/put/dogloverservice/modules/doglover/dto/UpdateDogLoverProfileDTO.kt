package pl.poznan.put.dogloverservice.modules.doglover.dto

import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.util.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class UpdateDogLoverProfileDTO(
        @field:NotBlank
        val nickname: String,

        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @field:Positive
        val age: Int? = null,

        val hobby: String? = null
) {
    fun toDogLoverEntity(dogLoverId: UUID) =  DogLover(
            id = dogLoverId,
            nickname = this.nickname,
            firstName = this.firstName,
            lastName = this.lastName
    )

    fun toDogLoverEntity(dogLoverId: UUID, dogLover: DogLover) = DogLover(
            id = dogLoverId,
            nickname = dogLover.nickname,
            firstName = this.firstName,
            lastName = this.lastName,
            likesCount = dogLover.likesCount,
            avatar = dogLover.avatar
    )
}
