package pl.poznan.put.dogloverservice.modules.dog

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogAvatarNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.RemoveLastDogException
import pl.poznan.put.dogloverservice.infrastructure.response.FileResponseEntityBuilder
import pl.poznan.put.dogloverservice.modules.common.avatar.AvatarImage
import pl.poznan.put.dogloverservice.modules.dog.dto.CreateDogDTO
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEventRepository
import java.time.Instant
import java.util.*

@Service
class DogService(
        private val dogRepository: DogRepository,
        private val dogLoverService: DogLoverService,
        private val calendarEventRepository: UserCalendarEventRepository
) {

    fun addDog(dogDTO: CreateDogDTO, dogLoverId: UUID): DogDTO {
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

    fun updateDog(dogDTO: CreateDogDTO, dogLoverId: UUID): DogDTO {
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

    fun getDogsInfo(dogLoverId: UUID): List<DogDTO> {
        return getDogs(dogLoverId).map { DogDTO(it) }
    }

    fun getDogs(dogLoverId: UUID): List<Dog> {
        return dogRepository.findAllByDogLoverIdAndRemovedIsFalse(dogLoverId)
    }

    fun getDog(dogId: UUID): Dog {
        return dogRepository.findByIdAndRemovedIsFalse(dogId)
                ?: throw DogNotFoundException(dogId)
    }

    fun getDog(name: String, dogLoverId: UUID): Dog {
        return dogRepository.findByNameAndDogLoverIdAndRemovedIsFalse(name, dogLoverId)
                ?: throw DogNotFoundException(name, dogLoverId)
    }

    @Transactional
    fun removeDog(dogName: String, dogLoverId: UUID) {
        val dog = getDog(dogName, dogLoverId)
        validateDogNotTheLastOne(dogLoverId)
        removeAllFutureEventsForDog(dog)

        dogRepository.save(Dog(dog, true))
    }

    fun getDogAvatar(dogId: UUID): ResponseEntity<ByteArray> {
        val dog = getDog(dogId)
        val dogAvatar = dog.avatar ?: throw DogAvatarNotFoundException(dogId)
        val (contentType, filename) = dogAvatar.getProperties(dog.id)

        return FileResponseEntityBuilder.build(
                httpStatus = HttpStatus.OK,
                filename = filename,
                contentType = contentType,
                body = dogAvatar.image
        )
    }

    fun saveDogAvatar(dogName: String, avatar: MultipartFile, dogLoverId: UUID) {
        validateDogExists(dogName, dogLoverId)
        val dogAvatar = AvatarImage(avatar.bytes)
        dogRepository.saveAvatar(dogName, dogAvatar.image, dogAvatar.checksum, dogLoverId)
    }

    @Transactional
    protected fun removeAllFutureEventsForDog(dog: Dog) {
        calendarEventRepository.deleteAllByDogAndDateTimeAfter(dog, Instant.now())
    }

    private fun validateDogExists(name: String, dogLoverId: UUID) {
        if (!dogRepository.existsByNameAndDogLoverIdAndRemovedIsFalse(name, dogLoverId))
            throw DogNotFoundException(name, dogLoverId)
    }

    private fun validateDogNotAlreadyExists(name: String, dogLoverId: UUID) {
        if (dogRepository.existsByNameAndDogLoverIdAndRemovedIsFalse(name, dogLoverId))
            throw DogAlreadyExistsException(name, dogLoverId)
    }

    private fun validateDogNotTheLastOne(dogLoverId: UUID) {
        if (dogRepository.countAllByDogLoverIdAndRemovedIsFalse(dogLoverId) < 2)
            throw RemoveLastDogException()
    }
}