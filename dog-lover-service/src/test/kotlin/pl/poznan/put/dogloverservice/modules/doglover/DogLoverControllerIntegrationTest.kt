package pl.poznan.put.dogloverservice.modules.doglover

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import java.util.UUID
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData.getUpdateJohnProfileDTO
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.transaction.Transactional

@Transactional
@RunWith(SpringRunner::class)
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "11443bdb-a4c9-4921-8a14-239d10189053")
@ContextConfiguration
@AutoConfigureMockMvc
class DogLoverControllerIntegrationTest(
        val dogLoverRepository: DogLoverRepository,
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    @WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
    fun `Should get dog lover profile`() {
        //given
        val dogLoverId = UUID.fromString("9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")

        //when
        val response = mockMvc.perform(get("/profiles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val dogLoverProfile = jacksonObjectMapper().readValue<DogLoverProfileDTO>(response)

        //then
        dogLoverProfile.id shouldBe dogLoverId
    }

    @Test
    //@WithUserDetails(value = "11443bdb-a4c9-4921-8a14-239d10189053")
    @WithMockUser(authorities = ["SCOPE_dog-lover"], value = "11443bdb-a4c9-4921-8a14-239d10189053")
    fun `Should create dog lover profile`() {
        //given
        val updateDogLoverProfileDTO = getUpdateJohnProfileDTO("john.kowalski")
        val dogLoverProfileDTO = DogLoverProfileDTO(DogLoverData.john)

        //when
        val response = mockMvc.perform(put("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateDogLoverProfileDTO)))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDogLoverProfile = jacksonObjectMapper().readValue<DogLoverProfileDTO>(response)

        //then
        returnedDogLoverProfile shouldBe dogLoverProfileDTO
    }

    @Test
    @WithMockUser(authorities = ["SCOPE_dog-lover"], value = "11443bdb-a4c9-4921-8a14-239d10189053")
    fun `Should throw while creating dog lover profile with existing username`() {
        //given
        val updateDogLoverProfileDTO = getUpdateJohnProfileDTO("adas.malysz11")

        //when
        mockMvc.perform(put("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(updateDogLoverProfileDTO)))

                //then
                .andExpect(status().isConflict)
                .andExpect(status().reason("Dog lover with nickname already exists."))
    }
}