package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationship
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus
import pl.poznan.put.dogloverservice.modules.walk.Walk
import pl.poznan.put.dogloverservice.modules.walk.WalkStatus

data class DogLoverInLocationDTO(

        val id: UUID,

        val nickname: String,

        val name: String,

        val dogs: List<DogBasicInfoDTO>,

        val relationshipStatus: RelationshipStatus?,

        val walkStatus: WalkStatus?,

        val likesCount: Int

) {

    constructor(walk: Walk, dogLoverRelationships: List<DogLoverRelationship>) : this(
            id = walk.dogLover.id,
            nickname = walk.dogLover.nickname,
            name = walk.dogLover.firstName,
            dogs = walk.dogs.map { DogBasicInfoDTO(it) },
            relationshipStatus = dogLoverRelationships.find {
                it.id.receiverDogLover.id == walk.dogLover.id
            }?.relationshipStatus,
            walkStatus = walk.walkStatus,
            likesCount = walk.dogLover.likesCount)
}