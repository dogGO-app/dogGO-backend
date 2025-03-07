package pl.poznan.put.dogloverservice.modules.dog.dto

import pl.poznan.put.dogloverservice.modules.dog.Dog
import java.util.*

data class DogBasicInfoDTO(

        val id: UUID,

        val name: String,

        val breed: String?,

        val color: String?,

        val avatarChecksum: String?

) {
    constructor(dog: Dog): this(
            id = dog.id,
            name = dog.name,
            breed = dog.breed,
            color = dog.color,
            avatarChecksum = dog.avatar?.checksum
    )
}