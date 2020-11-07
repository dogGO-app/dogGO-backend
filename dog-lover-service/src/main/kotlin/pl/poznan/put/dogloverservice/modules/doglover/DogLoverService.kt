package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNotFoundException
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import java.util.*

@Service
class DogLoverService(
        private val dogLoverRepository: DogLoverRepository
) {
    fun getDogLoverProfile(userId: UUID): DogLoverProfileDTO {
        return DogLoverProfileDTO(
                dogLoverRepository.findByIdOrNull(userId) ?: throw DogLoverNotFoundException()
        )
    }

    fun updateDogLoverProfile(dogLoverProfile: DogLoverProfileDTO, userId: UUID): DogLoverProfileDTO {
        return DogLoverProfileDTO(
                dogLoverRepository.save(DogLover(userId,
                        dogLoverProfile.firstName,
                        dogLoverProfile.lastName,
                        dogLoverProfile.age,
                        dogLoverProfile.hobby))
        )
    }

    fun getDogLover(userId: UUID): DogLover {
        return dogLoverRepository.findByIdOrNull(userId) ?: throw DogLoverNotFoundException()
    }
}