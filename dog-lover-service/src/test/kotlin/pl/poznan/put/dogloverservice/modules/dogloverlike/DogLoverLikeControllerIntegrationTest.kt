package pl.poznan.put.dogloverservice.modules.dogloverlike

import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class DogLoverLikeControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should add and remove dog lover like`() {
        //given
        val receiverDogLoverId = "6860ad51-bf45-46ba-b8d9-84567be4b037"

        //when
        mockMvc.perform(post("/dog-lover-likes")
                .param("receiverDogLoverId", receiverDogLoverId)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isCreated)

        //when
        mockMvc.perform(delete("/dog-lover-likes")
                .param("receiverDogLoverId", receiverDogLoverId)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isNoContent)
    }

    @Test
    fun `Should throw while adding like to not arrived dog lover`() {
        //given
        val receiverDogLoverId = "7e28c2b6-ed80-4cb8-9d74-f352d89fb522"

        //when
        mockMvc.perform(post("/dog-lover-likes")
                .param("receiverDogLoverId", receiverDogLoverId)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isNotFound)
                .andExpect(status().reason("Walk with ARRIVED_AT_DESTINATION status not found."))
    }

    @Test
    fun `Should throw while deleting not existing dog lover like`() {
        //given
        val receiverDogLoverId = "6860ad51-bf45-46ba-b8d9-84567be4b037"

        //when
        mockMvc.perform(delete("/dog-lover-likes")
                .param("receiverDogLoverId", receiverDogLoverId)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isNotFound)
                .andExpect(status().reason("Dog lover like doesn't exist."))
    }
}