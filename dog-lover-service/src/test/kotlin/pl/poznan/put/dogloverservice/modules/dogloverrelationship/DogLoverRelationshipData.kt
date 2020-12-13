package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus.BLOCKS
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus.FOLLOWS

object DogLoverRelationshipData {
    val johnWithAndrew
        get() = DogLoverRelationship(
                id = DogLoverRelationshipId(
                        giverDogLover = DogLoverData.john,
                        receiverDogLover = DogLoverData.andrew
                ),
                relationshipStatus = FOLLOWS
        )

    val johnWithTom
        get() = DogLoverRelationship(
                id = DogLoverRelationshipId(
                        giverDogLover = DogLoverData.john,
                        receiverDogLover = DogLoverData.tom
                ),
                relationshipStatus = BLOCKS
        )

    val johnRelationships
        get() = listOf(johnWithAndrew, johnWithTom)
}