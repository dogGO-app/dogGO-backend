package pl.poznan.put.dogloverservice.modules.doglover

import org.hibernate.annotations.ColumnDefault
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverLikesCountLowerThanZeroException
import pl.poznan.put.dogloverservice.modules.common.avatar.AvatarImage
import java.util.*
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank
import javax.validation.constraints.PositiveOrZero

@Entity
class DogLover(
        @Id
        @Column(name = "user_id")
        val id: UUID,

        @field:NotBlank
        @Column(unique = true, updatable = false, nullable = false)
        val nickname: String,

        @field:NotBlank
        val firstName: String,

        @field:NotBlank
        val lastName: String,

        @Embedded
        val avatar: AvatarImage? = null

) {
    @field:PositiveOrZero
    @ColumnDefault(value = "0")
    var likesCount: Int = 0
        private set

    fun addLike() = this.apply {
        likesCount += 1
    }

    fun removeLike() = this.apply {
        if (likesCount < 1)
            throw DogLoverLikesCountLowerThanZeroException()
        likesCount -= 1
    }
}
