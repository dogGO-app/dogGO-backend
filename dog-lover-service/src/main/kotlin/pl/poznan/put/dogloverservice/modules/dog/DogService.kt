package pl.poznan.put.dogloverservice.modules.dog

import java.time.Instant
import java.util.UUID
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogNotFoundException
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEventRepository

@Service
class DogService(
        private val dogRepository: DogRepository,
        private val dogLoverService: DogLoverService,
        private val calendarEventRepository: UserCalendarEventRepository
) {

    fun addDog(dogDTO: DogDTO, dogLoverId: UUID): DogDTO {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        validateDogNotAlreadyExists(dogDTO.name, dogLoverId)
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
        val dog = dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(dogDTO.name, dogLoverId)
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

    fun getDogLoverDogsInfo(dogLoverId: UUID): List<DogDTO> {
        return getDogLoverDogs(dogLoverId).map { DogDTO(it) }
    }

    fun getDogLoverDogs(dogLoverId: UUID): List<Dog> {
        return dogRepository.findAllByDogLoverIdAndRemovedIsFalse(dogLoverId)
    }

    fun getDogInfo(name: String, dogLoverId: UUID): DogDTO {
        return DogDTO(getDog(name, dogLoverId))
    }

    fun getDog(name: String, dogLoverId: UUID): Dog {
        return dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(name, dogLoverId)
                ?: throw DogNotFoundException(name, dogLoverId)
    }

    @Transactional
    fun removeDog(dogLoverId: UUID, dogName: String) {
        val dog = getDog(dogName, dogLoverId)
        removeAllFutureEventsForDog(dog)

        dogRepository.save(Dog(dog, true))
    }

    private fun validateDogNotAlreadyExists(name: String, dogLoverId: UUID) {
        if (dogRepository.existsByNameAndDogLoverIdAndRemovedIsFalse(name, dogLoverId))
            throw DogAlreadyExistsException(name, dogLoverId)
    }

    @Transactional
    protected fun removeAllFutureEventsForDog(dog: Dog) {
        val calendarEvents = calendarEventRepository.findAllByDogAndDateTimeAfter(dog, Instant.now())

        calendarEventRepository.deleteAll(calendarEvents)
    }
}