package pl.poznan.put.dogloverservice.doglover

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.doglover.dto.DogLoverProfileDTO
import pl.poznan.put.dogloverservice.exceptions.DogLoverNotFoundException

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
}