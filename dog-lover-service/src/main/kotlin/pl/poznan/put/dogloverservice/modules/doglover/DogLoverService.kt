package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNicknameAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNotFoundException
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import pl.poznan.put.dogloverservice.modules.doglover.dto.UpdateDogLoverProfileDTO
import java.util.*

@Service
class DogLoverService(
        private val dogLoverRepository: DogLoverRepository
) {
    fun getDogLoverProfile(dogLoverId: UUID): DogLoverProfileDTO {
        return DogLoverProfileDTO(
                getDogLover(dogLoverId)
        )
    }

    fun updateDogLoverProfile(updatedDogLoverProfile: UpdateDogLoverProfileDTO, dogLoverId: UUID): DogLoverProfileDTO {
        val currentDogLoverProfile = dogLoverRepository.findByIdOrNull(dogLoverId)
        currentDogLoverProfile?.let {
            return DogLoverProfileDTO(
                    dogLoverRepository.save(
                            updatedDogLoverProfile.toDogLoverEntity(
                                    dogLoverId,
                                    currentDogLoverProfile.nickname
                            )
                    )
            )
        }

        validateNicknameIsUnique(updatedDogLoverProfile.nickname)
        return DogLoverProfileDTO(
                dogLoverRepository.save(updatedDogLoverProfile.toDogLoverEntity(dogLoverId))
        )
    }

    fun getDogLover(dogLoverId: UUID): DogLover {
        return dogLoverRepository.findByIdOrNull(dogLoverId) ?: throw DogLoverNotFoundException()
    }

    fun getDogLover(nickname: String): DogLover {
        return dogLoverRepository.findByNickname(nickname) ?: throw DogLoverNotFoundException()
    }

    fun updateDogLover(dogLover: DogLover): DogLover {
        validateDogLoverExists(dogLover.id)
        return dogLoverRepository.save(dogLover)
    }

    private fun validateDogLoverExists(dogLoverId: UUID) {
        if (!dogLoverRepository.existsById(dogLoverId))
            throw DogLoverNotFoundException()
    }

    private fun validateNicknameIsUnique(nickname: String) {
        if (dogLoverRepository.existsByNickname(nickname))
            throw DogLoverNicknameAlreadyExistsException()
    }
}