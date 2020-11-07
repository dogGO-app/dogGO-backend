package pl.poznan.put.dogloverservice.modules.usercalendarevent

import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.Type
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotBlank

@Entity
@Table(name = "user_calendar")
class UserCalendarEvent(

        @Id
        @Type(type = "pg-uuid")
        val id: UUID = UUID.randomUUID(),

        val dateTime: Instant,

        @field:NotBlank
        val description: String,

        @ManyToOne
        val dog: Dog,

        @ManyToOne
        val dogLover: DogLover
)