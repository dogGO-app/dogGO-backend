package pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto

import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus
import java.util.*

data class DogLoverRelationshipDTO(

        val receiverDogLoverId: UUID,

        val receiverDogLoverNickname: String,

        val receiverDogLoverAvatarChecksum: String?,

        val receiverDogLoverDogs: List<DogBasicInfoDTO>,

        val relationshipStatus: RelationshipStatus
) {

    constructor(dogLoverRelationship: DogLoverRelationship, dogLoverDogs: List<Dog>) : this(
            receiverDogLoverId = dogLoverRelationship.id.receiverDogLover.id,
            receiverDogLoverNickname = dogLoverRelationship.id.receiverDogLover.nickname,
            receiverDogLoverAvatarChecksum = dogLoverRelationship.id.receiverDogLover.avatar?.checksum,
            receiverDogLoverDogs = dogLoverDogs.map { DogBasicInfoDTO(it) },
            relationshipStatus = dogLoverRelationship.relationshipStatus
    )
}