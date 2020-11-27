package pl.poznan.put.dogloverservice.modules.doglover

import java.util.*
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive

@Entity
class DogLover(
        @Id
        @Column(name = "user_id")
        val id: UUID,

        @field:NotBlank
        val nickname: String,

        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @field:Positive
        val age: Int,

        val hobby: String? = null
)
