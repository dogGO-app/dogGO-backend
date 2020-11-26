package pl.poznan.put.dogloverservice.modules.dogloverlike

import pl.poznan.put.dogloverservice.modules.walk.Walk
import java.io.Serializable
import java.time.Instant
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class DogLoverLike(
        @EmbeddedId
        val id: DogLoverLikeId,

        val createdAt: Instant = Instant.now()
)

@Embeddable
data class DogLoverLikeId(
        @OneToOne
        val giverDogLoverWalk: Walk,

        @OneToOne
        val receiverDogLoverWalk: Walk
) : Serializable