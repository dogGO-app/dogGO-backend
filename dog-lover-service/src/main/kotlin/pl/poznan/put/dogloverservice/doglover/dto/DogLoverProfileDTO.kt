package pl.poznan.put.dogloverservice.doglover.dto

import pl.poznan.put.dogloverservice.doglover.DogLover
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

data class DogLoverProfileDTO(
        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @field:Positive
        val age: Int,

        val hobby: String? = null
) {
    constructor(dogLover: DogLover) : this(
            dogLover.firstName,
            dogLover.lastName,
            dogLover.age,
            dogLover.hobby
    )
}