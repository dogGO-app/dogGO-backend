package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus
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
    constructor(dogLover: DogLover, dogs: List<DogBasicInfoDTO>, relationshipStatus: RelationshipStatus?, walkStatus: WalkStatus?) : this(
            id = dogLover.id,
            nickname = dogLover.nickname,
            name = dogLover.firstName,
            dogs = dogs,
            relationshipStatus = relationshipStatus,
            walkStatus = walkStatus,
            likesCount = dogLover.likesCount
    )
}