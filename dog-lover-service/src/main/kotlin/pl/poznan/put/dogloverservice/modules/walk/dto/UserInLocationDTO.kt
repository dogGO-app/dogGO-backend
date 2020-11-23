package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class UserInLocationDTO(

        val id: UUID,

        val name: String,

        val dogs: List<DogInLocationDTO>

) {
    constructor(dogLover: DogLover, dogs: List<DogInLocationDTO>) : this(
            dogLover.id,
            dogLover.firstName,
            dogs
    )
}