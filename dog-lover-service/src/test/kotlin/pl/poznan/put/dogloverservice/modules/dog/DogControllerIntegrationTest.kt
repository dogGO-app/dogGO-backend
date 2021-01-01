package pl.poznan.put.dogloverservice.modules.dog

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.should
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
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.dog.dto.UpdateDogDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class DogControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should add dog`() {
        //given
        val updateDogDTO = UpdateDogDTO(DogData.burek)
        val expectedDogDTO = DogDTO(DogData.burek)

        //when
        val response = mockMvc.perform(post("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(updateDogDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString
        val returnedDogDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<DogDTO>(response)

        //then
        returnedDogDTO.should {
            it.name shouldBe expectedDogDTO.name
            it.breed shouldBe expectedDogDTO.breed
            it.color shouldBe expectedDogDTO.color
            it.description shouldBe expectedDogDTO.description
            it.lastVaccinationDate shouldBe expectedDogDTO.lastVaccinationDate
            it.avatarChecksum shouldBe null
        }
    }

    @Test
    fun `Should throw while adding existing dog`() {
        //given
        val dogLoverId = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5"
        val updateDogDTO = UpdateDogDTO(DogData.yogi)

        //when
        mockMvc.perform(post("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(updateDogDTO))
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isConflict)
                .andExpect(status().reason("Dog with name: ${updateDogDTO.name} already exists for user with id: $dogLoverId."))
    }

    @Test
    fun `Should update dog`() {
        //given
        val updateDogDTO = UpdateDogDTO(DogData.yogi)
        val expectedDogDTO = DogDTO(DogData.yogi)

        //when
        val response = mockMvc.perform(put("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(updateDogDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDogDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<DogDTO>(response)

        //then
        returnedDogDTO shouldBe expectedDogDTO
    }

    @Test
    fun `Should get dog lover dogs`() {
        //given
        val dogName = "yogi"

        //when
        val response = mockMvc.perform(get("/dogs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDogs = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<List<DogDTO>>(response)

        //then
        returnedDogs.size shouldBe 2
        returnedDogs.map { it.name } shouldContain dogName
    }

    @Test
    fun `Should remove dog`() {
        //given
        val dogName = "yogi"

        //when
        mockMvc.perform(delete("/dogs/$dogName")
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isNoContent)
    }
}