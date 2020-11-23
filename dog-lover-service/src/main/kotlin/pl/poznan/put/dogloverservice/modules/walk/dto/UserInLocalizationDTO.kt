package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class UserInLocalizationDTO(

        val id: UUID,

        val name: String,

        val dogs: List<DogInLocalizationDTO>

) {
    constructor(dogLover: DogLover, dogs: List<DogInLocalizationDTO>) : this(
            dogLover.id,
            dogLover.firstName,
            dogs
    )
}