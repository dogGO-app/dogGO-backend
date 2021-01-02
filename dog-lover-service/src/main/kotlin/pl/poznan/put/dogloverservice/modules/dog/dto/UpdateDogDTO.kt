package pl.poznan.put.dogloverservice.modules.dog.dto

import pl.poznan.put.dogloverservice.modules.dog.Dog
import java.time.LocalDate

data class UpdateDogDTO(

        val name: String,

        val breed: String?,

        val color: String?,

        val description: String?,

        val lastVaccinationDate: LocalDate?,
) {

    constructor(dog: Dog) : this(
            name = dog.name,
            breed = dog.breed,
            color = dog.color,
            description = dog.description,
            lastVaccinationDate = dog.lastVaccinationDate
    )
}
