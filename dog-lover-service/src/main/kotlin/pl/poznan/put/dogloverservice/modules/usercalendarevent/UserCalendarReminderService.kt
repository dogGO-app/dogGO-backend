package pl.poznan.put.dogloverservice.modules.usercalendarevent

import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.client.AuthServiceClient
import pl.poznan.put.dogloverservice.infrastructure.client.MailServiceClient
import pl.poznan.put.dogloverservice.infrastructure.commons.RestTemplateTokenRequester
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.CalendarEventReminder
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.EventDetails

@Service
@EnableScheduling
class UserCalendarReminderService(
        @Value("\${self.time-zone}") private val timeZone: String,
        private val userCalendarEventRepository: UserCalendarEventRepository,
        private val restTemplateTokenRequester: RestTemplateTokenRequester,
        private val mailServiceClient: MailServiceClient,
        private val authServiceClient: AuthServiceClient
) {

    @Scheduled(cron = "\${self.calendar-events-reminder-cron}")
    fun sendCalendarEventsReminders() {
        val dogLoverTomorrowEvents = userCalendarEventRepository.findAllTomorrowEvents()
                .groupBy { it.dogLover }

        val accessToken = restTemplateTokenRequester.getAccessToken()
        val dogLoversEmails = authServiceClient.getUsersEmails("Bearer $accessToken",
                dogLoverTomorrowEvents.keys.map { it.id })

        val requestBody = dogLoverTomorrowEvents.map { (dogLover, calendarEvents) ->
            dogLoversEmails[dogLover.id]?.let { dogLoverEmail ->
                CalendarEventReminder(
                        dogLoverEmail,
                        dogLover.nickname,
                        calendarEvents.map { EventDetails(it, timeZone) }
                )
            }!!
        }

        mailServiceClient.sendCalendarEventsReminderEmail("Bearer $accessToken", requestBody)
    }
}