package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.Dog

data class DogInLocationDTO(

        val id: UUID,

        val name: String,

        val breed: String?

) {
    constructor(dog: Dog): this(
            dog.id,
            dog.name,
            dog.breed
    )
}