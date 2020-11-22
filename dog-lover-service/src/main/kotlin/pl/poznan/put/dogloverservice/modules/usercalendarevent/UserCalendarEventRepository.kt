package pl.poznan.put.dogloverservice.modules.usercalendarevent

import java.time.Instant
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

@Repository
interface UserCalendarEventRepository : JpaRepository<UserCalendarEvent, UUID> {

    fun findAllByDogLoverAndDateTimeAfter(dogLover: DogLover, dateTime: Instant): List<UserCalendarEvent>

    fun findByIdAndDogLover(id: UUID, dogLover: DogLover): UserCalendarEvent?

    fun existsByDateTimeAndDogLoverAndDog(dateTime: Instant, dogLover: DogLover, dog: Dog): Boolean

    fun existsByDateTimeAndDogLoverAndDogAndIdIsNot(dateTime: Instant, dogLover: DogLover, dog: Dog, eventId: UUID): Boolean

}