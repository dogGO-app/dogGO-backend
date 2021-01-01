package pl.poznan.put.dogloverservice.modules.dog

import org.hibernate.annotations.ColumnDefault
import org.hibernate.annotations.Type
import pl.poznan.put.dogloverservice.modules.common.avatar.AvatarImage
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.time.LocalDate
import java.util.*
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["name", "dog_lover_user_id"])])
class Dog(
        @Id
        @Type(type = "pg-uuid")
        val id: UUID = UUID.randomUUID(),

        val name: String,

        val breed: String?,

        val color: String?,

        val description: String?,

        val lastVaccinationDate: LocalDate?,

        @Column(nullable = false)
        @ColumnDefault(value = "false")
        val removed: Boolean = false,

        @ManyToOne
        val dogLover: DogLover,

        @Embedded
        val avatar: AvatarImage? = null
) {
        constructor(dog: Dog, removed: Boolean): this(
                id = dog.id,
                name = dog.name,
                breed = dog.breed,
                color = dog.color,
                description = dog.description,
                lastVaccinationDate = dog.lastVaccinationDate,
                removed = removed,
                dogLover = dog.dogLover
        )
}