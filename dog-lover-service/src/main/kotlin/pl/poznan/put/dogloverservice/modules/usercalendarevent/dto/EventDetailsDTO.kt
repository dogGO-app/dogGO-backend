package pl.poznan.put.dogloverservice.modules.usercalendarevent.dto

import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEvent

data class EventDetailsDTO(

        val description: String,

        val time: String,

        val dogName: String
) {
    constructor(userCalendarEvent: UserCalendarEvent, timeZone: String) : this(
            description = userCalendarEvent.description,
            time = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).format(
                    LocalTime.from(userCalendarEvent.dateTime.atZone(ZoneId.of(timeZone)))),
            dogName = userCalendarEvent.dog.name
    )
}
