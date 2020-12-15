package pl.poznan.put.mailservice.modules.mail

import org.apache.commons.validator.routines.EmailValidator
import pl.poznan.put.mailservice.modules.mail.dto.EventDetailsDTO
import pl.poznan.put.mailservice.modules.mail.message.AccountActivationEmail
import pl.poznan.put.mailservice.modules.mail.message.CalendarEventsReminderEmail
import pl.poznan.put.mailservice.modules.mail.message.EmailMessage

object EmailData {
    val correctAccountActivationEmail
        get() = AccountActivationEmail(
                receiverEmail = "test@test.pl",
                activationCode = "123456"
        )

    val incorrectAccountActivationEmail
        get() = AccountActivationEmail(
                receiverEmail = "incorrect_email",
                activationCode = "123456"
        )

    val correctEventsReminderEmail
        get() = CalendarEventsReminderEmail(
                receiverEmail = "test@test.pl",
                nickname = "test.guy",
                eventsDetails = listOf(
                        EventDetailsDTO(
                                description = "Description",
                                time = "12:44 PM",
                                dogName = "Burek"
                        ),
                        EventDetailsDTO(
                                description = "Other description",
                                time = "14:12 PM",
                                dogName = "Azor"
                        )
                )
        )

    val incorrectEventsReminderEmail
        get() = CalendarEventsReminderEmail(
                receiverEmail = "incorrect_email",
                nickname = "incorrect_email_username",
                eventsDetails = listOf(
                        EventDetailsDTO(
                                description = "Description",
                                time = "12:44 PM",
                                dogName = "Burek"
                        ),
                        EventDetailsDTO(
                                description = "Other description",
                                time = "14:12 PM",
                                dogName = "Azor"
                        )
                )
        )

    val emailMessages: List<EmailMessage>
        get() = listOf(
                correctAccountActivationEmail,
                incorrectAccountActivationEmail,
                correctEventsReminderEmail,
                incorrectEventsReminderEmail
        )

    val correctEmailMessages: List<EmailMessage>
        get() = emailMessages.filter {
            EmailValidator.getInstance().isValid(it.receiverEmail)
        }
}