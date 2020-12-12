package pl.poznan.put.mailservice.modules.mail.dto

data class CalendarEventReminderDTO(

        val dogLoverEmail: String,

        val dogLoverNickname: String,

        val eventsDetails: List<EventDetailsDTO>
)
