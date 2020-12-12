package pl.poznan.put.dogloverservice.modules.usercalendarevent.dto

data class CalendarEventReminderDTO(

        val dogLoverEmail: String,

        val dogLoverNickname: String,

        val eventsDetails: List<EventDetailsDTO>
)
