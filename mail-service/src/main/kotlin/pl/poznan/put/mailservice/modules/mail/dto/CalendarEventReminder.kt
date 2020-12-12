package pl.poznan.put.mailservice.modules.mail.dto

data class CalendarEventReminder(

        val dogLoverEmail: String,

        val dogLoverNickname: String,

        val eventsDetails: List<EventDetails>
)
