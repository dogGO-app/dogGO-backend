package pl.poznan.put.dogloverservice.modules.usercalendarevent

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.UserCalendarEventAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.UserCalendarEventDateTimeException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.UserCalendarEventNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.UserCalendarIdEmptyException
import pl.poznan.put.dogloverservice.modules.dog.Dog
import pl.poznan.put.dogloverservice.modules.dog.DogService
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverService
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.UserCalendarEventDTO
import java.time.*
import java.util.*

@Service
class UserCalendarEventService(
        @Value("\${self.time-zone}") private val timeZone: String,
        private val userCalendarEventRepository: UserCalendarEventRepository,
        private val dogService: DogService,
        private val dogLoverService: DogLoverService
) {

    fun getUserCalendar(dogLoverId: UUID): List<UserCalendarEventDTO> {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        return userCalendarEventRepository.findAllByDogLoverOrderByDateTimeAsc(dogLover)
                .map { UserCalendarEventDTO(it, timeZone) }
    }

    fun saveCalendarEvent(userCalendarEventDTO: UserCalendarEventDTO, dogLoverId: UUID): UserCalendarEventDTO {
        val dateTime = getInstantFromDateAndTime(userCalendarEventDTO.date, userCalendarEventDTO.time)
        validateDateTimeIsNotInPast(dateTime)

        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val dog = dogService.getDog(userCalendarEventDTO.dogName, dogLoverId)
        validateEventNotAlreadyExists(dateTime, dogLover, dog)
        val userCalendarEvent = UserCalendarEvent(
                dateTime = dateTime,
                description = userCalendarEventDTO.description,
                dog = dog,
                dogLover = dogLover
        )

        return UserCalendarEventDTO(userCalendarEventRepository.save(userCalendarEvent), timeZone)
    }

    fun updateCalendarEvent(userCalendarEventDTO: UserCalendarEventDTO, dogLoverId: UUID): UserCalendarEventDTO {
        val dateTime = getInstantFromDateAndTime(userCalendarEventDTO.date, userCalendarEventDTO.time)
        validateDateTimeIsNotInPast(dateTime)

        val dogLover = dogLoverService.getDogLover(dogLoverId)
        val calendarEvent = getCalendarEventEntity(userCalendarEventDTO.id
                ?: throw UserCalendarIdEmptyException(), dogLover)
        val dog = dogService.getDog(userCalendarEventDTO.dogName, dogLoverId)
        validateEventNotAlreadyExists(dateTime, dogLover, dog, calendarEvent.id)
        val updatedCalendarEvent = UserCalendarEvent(
                id = calendarEvent.id,
                dateTime = dateTime,
                description = userCalendarEventDTO.description,
                dog = dog,
                dogLover = dogLover
        )

        return UserCalendarEventDTO(userCalendarEventRepository.save(updatedCalendarEvent), timeZone)
    }

    fun getCalendarEvent(id: UUID, dogLoverId: UUID): UserCalendarEventDTO {
        val dogLover = dogLoverService.getDogLover(dogLoverId)
        return UserCalendarEventDTO(userCalendarEventRepository.findByIdAndDogLover(id, dogLover)
                ?: throw UserCalendarEventNotFoundException(id, dogLoverId),
                timeZone)
    }

    private fun validateDateTimeIsNotInPast(dateTime: Instant) {
        if (dateTime.isBefore(Instant.now()))
            throw UserCalendarEventDateTimeException()
    }

    private fun validateEventNotAlreadyExists(dateTime: Instant, dogLover: DogLover, dog: Dog, calendarEventId: UUID? = null) {
        calendarEventId?.let {
            if (userCalendarEventRepository.existsByDateTimeAndDogLoverAndDogAndIdIsNot(dateTime, dogLover, dog, calendarEventId))
                throw UserCalendarEventAlreadyExistsException(dateTime, dogLover.id, dog.name)
        }
                ?: if (userCalendarEventRepository.existsByDateTimeAndDogLoverAndDog(dateTime, dogLover, dog))
                    throw UserCalendarEventAlreadyExistsException(dateTime, dogLover.id, dog.name)

    }

    private fun getCalendarEventEntity(id: UUID, dogLover: DogLover): UserCalendarEvent {
        return userCalendarEventRepository.findByIdAndDogLover(id, dogLover)
                ?: throw UserCalendarEventNotFoundException(id, dogLover.id)
    }

    private fun getInstantFromDateAndTime(date: LocalDate, time: LocalTime): Instant {
        return LocalDateTime.of(date, time).atZone(ZoneId.of(timeZone)).toInstant()
    }

}