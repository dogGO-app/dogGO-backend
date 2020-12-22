package pl.poznan.put.mailservice.modules.mail.dto

data class CalendarEventsReminderDTO(

        val dogLoverEmail: String,

        val dogLoverNickname: String,

        val eventsDetails: List<EventDetailsDTO>
)
