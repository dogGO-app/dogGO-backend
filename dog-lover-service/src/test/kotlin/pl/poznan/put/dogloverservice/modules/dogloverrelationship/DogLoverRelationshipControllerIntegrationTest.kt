package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.hamcrest.Matchers.containsString
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto.DogLoverRelationshipDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class DogLoverRelationshipControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should get dog lover relationships`() {
        //when
        val response = mockMvc.perform(get("/relationships")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val dogLoverRelationships = jacksonObjectMapper().readValue<List<DogLoverRelationshipDTO>>(response)

        //then
        dogLoverRelationships.size shouldBe 1
        dogLoverRelationships[0].relationshipStatus shouldBe RelationshipStatus.FOLLOWS
    }

    @Test
    fun `Should add dog lover relationship`() {
        //given
        val receiverNickname = "janek.nowak"
        val relationshipStatus = RelationshipStatus.BLOCKS

        //when
        mockMvc.perform(post("/relationships/$receiverNickname")
                .param("status", relationshipStatus.name)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isCreated)
    }

    @Test
    fun `Should throw while adding second relationship to receiver`() {
        //given
        val receiverNickname = "jacol123"
        val relationshipStatus = RelationshipStatus.BLOCKS

        //when
        mockMvc.perform(post("/relationships/$receiverNickname")
                .param("status", relationshipStatus.name)
                .contentType(MediaType.APPLICATION_JSON))

                //then
                .andExpect(status().isConflict)
                .andExpect(status().reason(containsString("Dog lover relationship already exists in status")))
    }

    @Test
    fun `Should delete dog lover relationship`() {
        //given
        val receiverNickname = "jacol123"

        //when
        mockMvc.perform(delete("/relationships/$receiverNickname")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isOk)
    }

    @Test
    fun `Should throw while deleting not existing relationship`() {
        //given
        val receiverNickname = "janek.nowak"

        //when
        mockMvc.perform(delete("/relationships/$receiverNickname")
                .contentType(MediaType.APPLICATION_JSON))
                //then
                .andExpect(status().isNotFound)
                .andExpect(status().reason("Dog lover relationship not exists."))
    }
}