package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverRelationshipAlreadyExists
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverRelationshipNotExists
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto.DogLoverRelationshipDTO

@Service
class DogLoverRelationshipService(
        private val dogLoverRelationshipRepository: DogLoverRelationshipRepository,
        private val dogLoverService: DogLoverService,
        private val dogService: DogService
) {

    fun addDogLoverRelationship(dogLoverId: UUID, receiverNickname: String, relationshipStatus: RelationshipStatus) {
        val receiverDogLover = dogLoverService.getDogLover(receiverNickname)
        val giverDogLover = dogLoverService.getDogLover(dogLoverId)
        val dogLoverRelationshipId = DogLoverRelationshipId(giverDogLover, receiverDogLover)
        checkIfRelationshipAlreadyExists(dogLoverRelationshipId)

        dogLoverRelationshipRepository.save(DogLoverRelationship(
                dogLoverRelationshipId,
                relationshipStatus
        ))
    }

    fun removeDogLoverRelationship(dogLoverId: UUID, receiverNickname: String) {
        val receiverDogLover = dogLoverService.getDogLover(receiverNickname)
        val giverDogLover = dogLoverService.getDogLover(dogLoverId)
        val dogLoverRelationshipId = DogLoverRelationshipId(giverDogLover, receiverDogLover)
        checkDogLoverRelationshipNotExists(dogLoverRelationshipId)

        dogLoverRelationshipRepository.deleteById(dogLoverRelationshipId)
    }

    fun getDogLoverRelationships(dogLoverId: UUID): List<DogLoverRelationshipDTO> {
        val dogLoverRelationships = dogLoverRelationshipRepository.findAllByDogLoverRelationshipId_GiverDogLoverId(dogLoverId)

        return dogLoverRelationships.map {
            DogLoverRelationshipDTO(it,
                    dogService.getUserDogsEntity(it.dogLoverRelationshipId.receiverDogLover.id))
        }
    }

    private fun checkIfRelationshipAlreadyExists(dogLoverRelationshipId: DogLoverRelationshipId) {
        dogLoverRelationshipRepository.findByDogLoverRelationshipId(dogLoverRelationshipId)
                ?.let {
                    throw DogLoverRelationshipAlreadyExists(it.relationshipStatus)
                }
    }

    private fun checkDogLoverRelationshipNotExists(dogLoverRelationshipId: DogLoverRelationshipId) {
        if (!dogLoverRelationshipRepository.existsById(dogLoverRelationshipId))
            throw DogLoverRelationshipNotExists()
    }
}