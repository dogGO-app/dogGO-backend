package pl.poznan.put.dogloverservice.modules.dog

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
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
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
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
        val dogDTO = DogDTO(DogData.burek)

        //when
        val response = mockMvc.perform(post("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(dogDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString
        val returnedDogDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<DogDTO>(response)

        //then
        returnedDogDTO shouldBe dogDTO
    }

    @Test
    fun `Should throw while adding existing dog`() {
        //given
        val dogLoverId = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5"
        val dogDTO = DogDTO(DogData.yogi)

        //when
        mockMvc.perform(post("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(dogDTO))
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isConflict)
                .andExpect(status().reason("Dog with name: ${dogDTO.name} already exists for user with id: $dogLoverId."))
    }

    @Test
    fun `Should update dog`() {
        //given
        val dogDTO = DogDTO(DogData.yogi)

        //when
        val response = mockMvc.perform(put("/dogs")
                .content(jacksonObjectMapper().registerModule(JavaTimeModule()).writeValueAsString(dogDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDogDTO = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<DogDTO>(response)

        //then
        returnedDogDTO shouldBe dogDTO
    }

    @Test
    fun `Should get dog lover dog`() {
        //given
        val dogName = "yogi"

        //when
        val response = mockMvc.perform(get("/dogs/$dogName")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedDog = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<DogDTO>(response)

        //then
        returnedDog.name shouldBe dogName
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
}