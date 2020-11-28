package pl.poznan.put.dogloverservice.modules.doglover.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class DogLoverProfileDTO(

        @field:NotBlank
        val nickname: String,

        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @field:Positive
        val age: Int,

        val hobby: String? = null
) {
    constructor(dogLover: DogLover) : this(
            dogLover.nickname,
            dogLover.firstName,
            dogLover.lastName,
            dogLover.age,
            dogLover.hobby
    )

    fun toDogLoverEntity(dogLoverId: UUID, nickname: String? = null) = DogLover(
            id = dogLoverId,
            nickname = nickname ?: this.nickname,
            firstName = this.firstName,
            lastName = this.lastName,
            age = this.age,
            hobby = this.hobby
    )
}