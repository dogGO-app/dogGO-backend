package pl.poznan.put.mailservice.modules.mail

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.mailservice.modules.mail.dto.CalendarEventReminderDTO
import pl.poznan.put.mailservice.modules.mail.message.AccountActivationEmail
import pl.poznan.put.mailservice.modules.mail.message.CalendarEventsReminderEmail

@RestController
class MailController(
        private val mailService: MailService
) {
    @ApiResponses(
            ApiResponse(description = "Sent successfully.", responseCode = "200"),
            ApiResponse(description = "Invalid email address.", responseCode = "400"),
            ApiResponse(description = "Sending mail failed.", responseCode = "500"))
    @PostMapping("/account-activation")
    fun sendAccountActivationMail(@RequestParam receiver: String, @RequestParam activationCode: String) {
        mailService.sendEmail(AccountActivationEmail(receiver, activationCode))
    }

    @ApiResponses(
            ApiResponse(description = "Sent successfully.", responseCode = "200"),
            ApiResponse(description = "Sending mail failed.", responseCode = "500"))
    @PostMapping("/calendar-events-reminder")
    fun sendCalendarEventsReminderEmails(@RequestBody calendarEventsReminders: List<CalendarEventReminderDTO>) {
        mailService.sendEmails(calendarEventsReminders.map {
            CalendarEventsReminderEmail(
                    receiverEmail = it.dogLoverEmail,
                    nickname = it.dogLoverNickname,
                    eventDetails = it.eventsDetails)
        })
    }
}