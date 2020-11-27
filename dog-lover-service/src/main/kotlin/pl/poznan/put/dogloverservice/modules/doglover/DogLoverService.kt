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
    fun getDogLoverProfile(userId: UUID): DogLoverProfileDTO {
        return DogLoverProfileDTO(
                dogLoverRepository.findByIdOrNull(userId) ?: throw DogLoverNotFoundException()
        )
    }

    fun updateDogLoverProfile(dogLoverProfile: DogLoverProfileDTO, userId: UUID): DogLoverProfileDTO {
        val currentDogLoverProfile = dogLoverRepository.findByIdOrNull(userId)
        currentDogLoverProfile?.let {
            return DogLoverProfileDTO(
                    dogLoverRepository.save(dogLoverProfile.toDogLoverEntity(userId, currentDogLoverProfile.nickname))
            )
        } ?: checkIfNicknameIsUnique(dogLoverProfile.nickname)
        return DogLoverProfileDTO(
                dogLoverRepository.save(dogLoverProfile.toDogLoverEntity(userId))
        )
    }

    fun getDogLover(userId: UUID): DogLover {
        return dogLoverRepository.findByIdOrNull(userId) ?: throw DogLoverNotFoundException()
    }

    fun getDogLover(nickname: String): DogLover {
        return dogLoverRepository.findByNickname(nickname) ?: throw DogLoverNotFoundException()
    }

    private fun checkIfNicknameIsUnique(nickname: String) {
        if (dogLoverRepository.existsByNickname(nickname))
            throw DogLoverNicknameAlreadyExistsException()
    }
}