package pl.poznan.put.dogloverservice.modules.usercalendarevent

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import java.time.Instant
import java.util.UUID
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.dog.DogData
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.usercalendarevent.UserCalendarEventData.getDogLoverEvent
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.UserCalendarEventDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class UserCalendarEventControllerIntegrationTest(
        @Value("\${self.time-zone}") private val timeZone: String,
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should get calendar event`() {
        //given
        val userCalendarEventId = UUID.fromString("50a7eaf8-3e9f-4007-b875-f1ec67ef15ce")

        //when
        val response = mockMvc.perform(get("/user-calendar-events/$userCalendarEventId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val foundCalendarEvent = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<UserCalendarEventDTO>(response)

        //then
        foundCalendarEvent.id shouldBe userCalendarEventId
    }

    @Test
    fun `Should get user calendar`() {
        //when
        val response = mockMvc.perform(get("/user-calendar-events")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val foundCalendarEvents = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<List<UserCalendarEventDTO>>(response)

        //then
        foundCalendarEvents.size shouldBe 1
    }

    @Test
    fun `Should save user calendar event`() {
        //given
        val dogLover = DogLoverData.adam
        val dog = DogData.yogi
        val userCalendarEventDTO = UserCalendarEventDTO(getDogLoverEvent(
                dogLover, dog, Instant.now().plusSeconds(10000)),
                timeZone)

        //when
        val response = mockMvc.perform(post("/user-calendar-events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(userCalendarEventDTO)))
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString
        val returnedCalendarEvent = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<UserCalendarEventDTO>(response)

        //then
        returnedCalendarEvent.description shouldBe userCalendarEventDTO.description
        returnedCalendarEvent.date shouldBe userCalendarEventDTO.date
    }

    @Test
    fun `Should throw while saving calendar event from the past`() {
        //given
        val dogLover = DogLoverData.adam
        val dog = DogData.yogi
        val userCalendarEventDTO = UserCalendarEventDTO(getDogLoverEvent(
                dogLover, dog, Instant.now().minusSeconds(10000)),
                timeZone)

        //when
        mockMvc.perform(post("/user-calendar-events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(userCalendarEventDTO)))

                //then
                .andExpect(status().isBadRequest)
                .andExpect(status().reason("Cannot save calendar event with date and time in the past"))
    }

    @Test
    fun `Should update user calendar event`() {
        //given
        val dogLover = DogLoverData.adam
        val dog = DogData.yogi
        val userCalendarEventDTO = UserCalendarEventDTO(getDogLoverEvent(
                dogLover, dog, Instant.now().plusSeconds(10000),
                UUID.fromString("50a7eaf8-3e9f-4007-b875-f1ec67ef15ce")),
                timeZone)

        //when
        val response = mockMvc.perform(put("/user-calendar-events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(userCalendarEventDTO)))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedCalendarEvent = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<UserCalendarEventDTO>(response)

        //then
        returnedCalendarEvent.description shouldBe userCalendarEventDTO.description
        returnedCalendarEvent.date shouldBe userCalendarEventDTO.date
    }
}