package pl.poznan.put.dogloverservice.modules.usercalendarevent.dto

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.util.UUID
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEvent
import javax.validation.constraints.NotBlank

data class UserCalendarEventDTO(

        val id: UUID? = null,

        val date: LocalDate,

        val time: LocalTime,

        @field:NotBlank
        val description: String,

        val dogName: String

) {
    constructor(userCalendarEvent: UserCalendarEvent, timeZone: String) : this(
            id = userCalendarEvent.id,
            date = userCalendarEvent.dateTime.atZone(ZoneId.of(timeZone)).toLocalDate(),
            time = userCalendarEvent.dateTime.atZone(ZoneId.of(timeZone)).toLocalTime(),
            description = userCalendarEvent.description,
            dogName = userCalendarEvent.dog.name
    )
}