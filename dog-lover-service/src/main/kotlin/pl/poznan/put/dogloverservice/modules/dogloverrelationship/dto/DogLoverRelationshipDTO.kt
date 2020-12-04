package pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus

data class DogLoverRelationshipDTO(

        val receiverDogLoverId: UUID,

        val receiverDogLoverNickname: String,

        val receiverDogLoverDogs: List<DogBasicInfoDTO>,

        val relationshipStatus: RelationshipStatus
) {

    constructor(dogLoverRelationship: DogLoverRelationship, dogLoverDogs: List<Dog>) : this(
            dogLoverRelationship.id.receiverDogLover.id,
            dogLoverRelationship.id.receiverDogLover.nickname,
            dogLoverDogs.map { DogBasicInfoDTO(it) },
            dogLoverRelationship.relationshipStatus
    )
}