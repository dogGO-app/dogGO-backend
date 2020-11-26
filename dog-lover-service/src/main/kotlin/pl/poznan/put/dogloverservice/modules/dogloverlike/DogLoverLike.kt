package pl.poznan.put.dogloverservice.modules.dogloverlike

import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.walk.Walk
import java.io.Serializable
import javax.persistence.Embeddable
import javax.persistence.EmbeddedId
import javax.persistence.Entity
import javax.persistence.OneToOne

@Entity
class DogLoverLike(
        @EmbeddedId
        val id: DogLoverLikeId
) : Serializable

@Embeddable
data class DogLoverLikeId(
        @OneToOne
        val walk: Walk,
        @OneToOne
        val giverDogLover: DogLover,
        @OneToOne
        val receiverDogLover: DogLover
) : Serializable