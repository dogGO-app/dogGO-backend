package pl.poznan.put.dogloverservice.modules.usercalendarevent.dto

data class CalendarEventReminder(

        val dogLoverEmail: String,

        val dogLoverNickname: String,

        val eventsDetails: List<EventDetails>
)
