package pl.poznan.put.dogloverservice.modules.doglover

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import java.util.UUID
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData.getUpdateAdamProfileDTO
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class DogLoverControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
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
    fun `Should update dog lover profile`() {
        //given
        val updateDogLoverProfileDTO = getUpdateAdamProfileDTO()

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
        returnedDogLoverProfile.age shouldBe 20
        returnedDogLoverProfile.lastName shouldBe "Nowak"
    }
}