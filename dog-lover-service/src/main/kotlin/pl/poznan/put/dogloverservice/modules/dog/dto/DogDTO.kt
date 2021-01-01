package pl.poznan.put.dogloverservice.modules.dog.dto

import pl.poznan.put.dogloverservice.modules.dog.Dog
import java.time.LocalDate
import java.util.*

data class DogDTO(
        val id: UUID,

        val name: String,

        val breed: String?,

        val color: String?,

        val description: String?,

        val lastVaccinationDate: LocalDate?,

        val avatarChecksum: String?
) {

    constructor(dog: Dog) : this(
            id = dog.id,
            name = dog.name,
            breed = dog.breed,
            color = dog.color,
            description = dog.description,
            lastVaccinationDate = dog.lastVaccinationDate,
            avatarChecksum = dog.avatar?.checksum
    )
}
