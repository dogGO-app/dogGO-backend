package pl.poznan.put.dogloverservice.modules.walk

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldNotContain
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.walk.dto.CreateWalkDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO
import java.util.*
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class WalkControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should get completed walks history`() {
        //when
        val response = mockMvc.perform(get("/walks/history")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedWalks = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<List<WalkDTO>>(response)

        //then
        returnedWalks.size shouldBe 2
    }

    @Test
    fun `Should get others dog lovers in location`() {
        //given
        val dogLoverId = UUID.fromString("9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
        val mapMarkerId = "87e5efcc-f8b4-48d4-9986-d2997b8cfda5"

        //when
        val response = mockMvc.perform(get("/walks/dog-lovers-in-location/$mapMarkerId")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDogLovers = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<List<DogLoverInLocationDTO>>(response)

        //then
        returnedDogLovers.size shouldBe 1
        returnedDogLovers.map { it.id } shouldNotContain dogLoverId
    }

    @Test
    fun `Should save walk`() {
        //given
        val dogsNames = listOf("yogi", "azor")
        val mapMarkerId = UUID.fromString("87e5efcc-f8b4-48d4-9986-d2997b8cfda5")
        val createWalkDTO = CreateWalkDTO(dogsNames, mapMarkerId)

        //when
        val response = mockMvc.perform(post("/walks")
                .content(jacksonObjectMapper().writeValueAsString(createWalkDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString
        val returnedWalkDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<WalkDTO>(response)

        //then
        returnedWalkDTO.dogs.map { it.name } shouldBe dogsNames
        returnedWalkDTO.mapMarker.id shouldBe mapMarkerId
        returnedWalkDTO.walkStatus shouldBe WalkStatus.ONGOING
    }

    @Test
    fun `Should update walk status`() {
        //given
        val walkId = UUID.fromString("2f009fed-7a43-4bd2-9e3f-623ef688e953")
        val newWalkStatus = WalkStatus.ARRIVED_AT_DESTINATION

        //when
        val response = mockMvc.perform(put("/walks/$walkId")
                .param("walkStatus", newWalkStatus.name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedWalkDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<WalkDTO>(response)

        //then
        returnedWalkDTO.walkStatus shouldBe newWalkStatus
        returnedWalkDTO.id shouldBe walkId
    }

    @Test
    fun `Should throw while updating walk into forbidden status`() {
        //given
        val walkId = UUID.fromString("2f009fed-7a43-4bd2-9e3f-623ef688e953")
        val newWalkStatus = WalkStatus.LEFT_DESTINATION

        //when
        mockMvc.perform(put("/walks/$walkId")
                .param("walkStatus", newWalkStatus.name)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isForbidden)
                .andExpect(status().reason("Cannot update walk status - new status is forbidden."))
    }

    @Test
    fun `Should keep walk active`() {
        //when
        mockMvc.perform(put("/walks/active")
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isOk)
    }
}