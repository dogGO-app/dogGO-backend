package pl.poznan.put.dogloverservice.modules.usercalendarevent

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import pl.poznan.put.dogloverservice.infrastructure.client.AuthServiceClient
import pl.poznan.put.dogloverservice.infrastructure.client.MailServiceClient
import pl.poznan.put.dogloverservice.infrastructure.commons.RestTemplateTokenRequester
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEventData.getDogLoverEvent
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.CalendarEventReminderDTO
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.EventDetailsDTO

class UserCalendarReminderServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val timeZone = "Europe/Warsaw"
    val userCalendarEventRepository = mockk<UserCalendarEventRepository>(relaxed = true)
    val restTemplateTokenRequester = mockk<RestTemplateTokenRequester>(relaxed = true)
    val mailServiceClient = mockk<MailServiceClient>(relaxed = true)
    val authServiceClient = mockk<AuthServiceClient>(relaxed = true)

    val userCalendarReminderService = UserCalendarReminderService(
            timeZone,
            userCalendarEventRepository,
            restTemplateTokenRequester,
            mailServiceClient,
            authServiceClient
    )

    Given("appropriate time to send events reminders") {
        val accessToken = "access token"
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val dog = DogData.burek
        val dogLoverEmail = "johnEmail"
        val dogLoverEmails = mapOf(dogLoverId to dogLoverEmail)
        val tomorrowEvents = listOf(getDogLoverEvent(dogLover, dog), getDogLoverEvent(dogLover, dog))
        val tomorrowEventsGrouped = tomorrowEvents.groupBy { it.dogLover }

        val expectedRequestBody = tomorrowEventsGrouped.mapNotNull { (dogLover, calendarEvents) ->
            dogLoverEmails[dogLoverId]?.let { dogLoverEmail ->
                CalendarEventReminderDTO(
                        dogLoverEmail,
                        dogLover.nickname,
                        calendarEvents.map { EventDetailsDTO(it, timeZone) }
                )
            }
        }

        every {
            userCalendarEventRepository.findAllTomorrowEvents()
        } returns tomorrowEvents
        every {
            restTemplateTokenRequester.getAccessToken()
        } returns accessToken
        every {
            authServiceClient.getUsersEmails("Bearer $accessToken", listOf(dogLoverId))
        } returns dogLoverEmails

        When("sending events reminders mails") {
            userCalendarReminderService.sendCalendarEventsReminders()

            Then("MailServiceClient::sendCalendarEventsReminderEmails should be called") {
                verify(exactly = 1) {
                    mailServiceClient.sendCalendarEventsReminderEmails("Bearer $accessToken", eq(expectedRequestBody))
                }
            }
        }
    }
})