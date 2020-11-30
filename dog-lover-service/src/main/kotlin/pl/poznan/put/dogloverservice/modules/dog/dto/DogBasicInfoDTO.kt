package pl.poznan.put.dogloverservice.modules.dog.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.Dog

data class DogBasicInfoDTO(

        val id: UUID,

        val name: String,

        val breed: String?,

        val color: String?

) {
    constructor(dog: Dog): this(
            dog.id,
            dog.name,
            dog.breed,
            dog.color
    )
}