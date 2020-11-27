package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import java.io.Serializable
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.ManyToOne

@Entity
class DogLoverRelationship(

        @EmbeddedId
        val dogLoverRelationshipId: DogLoverRelationshipId,

        @Enumerated(EnumType.STRING)
        val relationshipStatus: RelationshipStatus
)

@Embeddable
data class DogLoverRelationshipId(

        @ManyToOne
        val giverDogLover: DogLover,

        @ManyToOne
        val receiverDogLover: DogLover

) : Serializable