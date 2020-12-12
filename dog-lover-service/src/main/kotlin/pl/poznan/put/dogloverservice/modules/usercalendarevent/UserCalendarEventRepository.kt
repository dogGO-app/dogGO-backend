package pl.poznan.put.dogloverservice.modules.usercalendarevent

import java.time.Instant
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

@Repository
interface UserCalendarEventRepository : JpaRepository<UserCalendarEvent, UUID> {

    fun findAllByDogLoverOrderByDateTimeAsc(dogLover: DogLover): List<UserCalendarEvent>

    fun findByIdAndDogLover(id: UUID, dogLover: DogLover): UserCalendarEvent?

    fun existsByDateTimeAndDogLoverAndDog(dateTime: Instant, dogLover: DogLover, dog: Dog): Boolean

    fun existsByDateTimeAndDogLoverAndDogAndIdIsNot(dateTime: Instant, dogLover: DogLover, dog: Dog, eventId: UUID): Boolean

    @Query(value = "SELECT * FROM user_calendar e WHERE DATE(e.date_time) = CURRENT_DATE + 1 ORDER BY e.date_time ASC",
            nativeQuery = true)
    fun findAllTomorrowEvents(): List<UserCalendarEvent>
}