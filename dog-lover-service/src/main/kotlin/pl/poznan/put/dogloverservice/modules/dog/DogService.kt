package pl.poznan.put.dogloverservice.modules.dog

import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogNotFoundException
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService

@Service
class DogService(
        private val dogRepository: DogRepository,
        private val dogLoverService: DogLoverService
) {

    fun addDog(dogDTO: DogDTO, dogLoverId: UUID): DogDTO {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        checkIfDogExists(dogDTO.name, dogLoverId)
        val dog = Dog(
                name = dogDTO.name,
                breed = dogDTO.breed,
                color = dogDTO.color,
                description = dogDTO.description,
                lastVaccinationDate = dogDTO.lastVaccinationDate,
                dogLover = dogLover
        )
        return DogDTO(dogRepository.save(dog))
    }

    fun updateDog(dogDTO: DogDTO, dogLoverId: UUID): DogDTO {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dog = dogRepository.findByNameAndDogLoverId(dogDTO.name, dogLoverId)
                ?: throw DogNotFoundException(dogDTO.name, dogLoverId)
        val updatedDog = Dog(
                id = dog.id,
                name = dog.name,
                breed = dogDTO.breed,
                color = dogDTO.color,
                description = dogDTO.description,
                lastVaccinationDate = dogDTO.lastVaccinationDate,
                dogLover = dogLover
        )
        return DogDTO(dogRepository.save(updatedDog))
    }

    fun getUserDogs(dogLoverId: UUID): List<DogDTO> {
        return dogRepository.findAllByDogLoverId(dogLoverId).map { DogDTO(it) }
    }

    fun getUserDogsEntity(dogLoverId: UUID): List<Dog> {
        return dogRepository.findAllByDogLoverId(dogLoverId)
    }

    fun getDog(name: String, dogLoverId: UUID): DogDTO {
        return DogDTO(dogRepository.findByNameAndDogLoverId(name, dogLoverId)
                ?: throw DogNotFoundException(name, dogLoverId))
    }

    fun getDogEntity(name: String, dogLoverId: UUID): Dog {
        return dogRepository.findByNameAndDogLoverId(name, dogLoverId)
                ?: throw DogNotFoundException(name, dogLoverId)
    }

    private fun checkIfDogExists(name: String, dogLoverId: UUID) {
        if (dogRepository.existsByNameAndDogLoverId(name, dogLoverId))
            throw DogAlreadyExistsException(name, dogLoverId)
    }
}