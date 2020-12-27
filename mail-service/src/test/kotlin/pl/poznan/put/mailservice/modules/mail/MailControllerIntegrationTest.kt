package pl.poznan.put.mailservice.modules.mail

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.spring.SpringListener
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import pl.poznan.put.mailservice.infrastructure.exceptions.InvalidEmailException
import javax.mail.internet.MimeMessage

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_user"])
@AutoConfigureMockMvc
class MailControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {
    override fun listeners() = listOf(SpringListener)

    @MockkBean(relaxed = true)
    lateinit var javaMailSender: JavaMailSender

    @Test
    fun `sending correct account activation email should return ok status`() {
        // Given
        val receiverEmail = "test@test.pl"
        val activationCode = "123456"

        // When
        mockMvc.post("/account-activation") {
            param("receiver", receiverEmail)
            param("activationCode", activationCode)
        }
                // Then
                .andExpect {
                    status { isOk }
                }

        verify(exactly = 1) { javaMailSender.send(any<MimeMessage>()) }
    }

    @Test
    fun `sending incorrect account activation email should return bad request status`() {
        // Given
        val receiverEmail = "notamail"
        val activationCode = "123456"

        // When
        mockMvc.post("/account-activation") {
            param("receiver", receiverEmail)
            param("activationCode", activationCode)
        }
                // Then
                .andExpect {
                    status {
                        isBadRequest
                        reason(InvalidEmailException(receiverEmail).message)
                    }
                }

        verify(exactly = 0) { javaMailSender.send(any<MimeMessage>()) }
    }

    @Test
    fun `sending calendar events reminder emails should return ok status`() {
        // Given
        val calendarEventsReminderDTOs = EmailData.correctCalendarEventsReminderDTOs
        val remindersCount = calendarEventsReminderDTOs.size

        // When
        mockMvc.post("/calendar-events-reminder") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(calendarEventsReminderDTOs)
        }
                // Then
                .andExpect {
                    status { isOk }
                }

        verify(exactly = remindersCount) { javaMailSender.send(any<MimeMessage>()) }
    }
}