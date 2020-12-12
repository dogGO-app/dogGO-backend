package pl.poznan.put.mailservice.modules.mail.message

import pl.poznan.put.mailservice.modules.mail.dto.EventDetailsDTO

class CalendarEventsReminderEmail(
    receiverEmail: String,
    nickname: String,
    eventDetails: List<EventDetailsDTO>
) : EmailMessage(receiverEmail) {
    override val templateName = "calendar-events-reminder"
    override val subject = "Your tomorrow plans saved in the dogGO application"
    override val variables = mapOf(
        "nickname" to nickname,
        "eventsDetails" to eventDetails,
    )
}