package pl.poznan.put.dogloverservice.modules.doglover

import java.util.UUID
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNicknameAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverNotFoundException
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO

@Service
class DogLoverService(
        private val dogLoverRepository: DogLoverRepository
) {
    fun getDogLoverProfile(dogLoverId: UUID): DogLoverProfileDTO {
        return DogLoverProfileDTO(
                getDogLover(dogLoverId)
        )
    }

    fun updateDogLoverProfile(dogLoverProfile: DogLoverProfileDTO, dogLoverId: UUID): DogLoverProfileDTO {
        val currentDogLoverProfile = dogLoverRepository.findByIdOrNull(dogLoverId)
        currentDogLoverProfile?.let {
            return DogLoverProfileDTO(
                    dogLoverRepository.save(dogLoverProfile.toDogLoverEntity(dogLoverId, currentDogLoverProfile.nickname))
            )
        } ?: checkIfNicknameIsUnique(dogLoverProfile.nickname)
        return DogLoverProfileDTO(
                dogLoverRepository.save(dogLoverProfile.toDogLoverEntity(dogLoverId))
        )
    }

    fun getDogLover(dogLoverId: UUID): DogLover {
        return dogLoverRepository.findByIdOrNull(dogLoverId) ?: throw DogLoverNotFoundException()
    }

    fun updateDogLover(dogLover: DogLover): DogLover {
        validateDogLoverExists(dogLover.id)
        return dogLoverRepository.save(dogLover)
    }

    private fun validateDogLoverExists(dogLoverId: UUID) {
        if (!dogLoverRepository.existsById(dogLoverId))
            throw DogLoverNotFoundException()
    }

    fun getDogLover(nickname: String): DogLover {
        return dogLoverRepository.findByNickname(nickname) ?: throw DogLoverNotFoundException()
    }

    private fun checkIfNicknameIsUnique(nickname: String) {
        if (dogLoverRepository.existsByNickname(nickname))
            throw DogLoverNicknameAlreadyExistsException()
    }
}