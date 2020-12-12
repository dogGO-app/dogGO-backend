package pl.poznan.put.dogloverservice.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.CalendarEventReminderDTO

@FeignClient(name = "mail-service")
interface MailServiceClient {

    @PostMapping("/calendar-events-reminder")
    fun sendCalendarEventsReminderEmail(
            @RequestHeader(value = "Authorization") token: String,
            @RequestBody calendarEventsReminders: List<CalendarEventReminderDTO>
    )
}