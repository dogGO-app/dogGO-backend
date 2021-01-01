package pl.poznan.put.dogloverservice.modules.doglover

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.multipart
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.infrastructure.exceptions.InvalidAvatarImageException
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData.getUpdateAdamProfileDTO
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import java.util.*
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
        returnedDogLoverProfile.lastName shouldBe "Nowak"
    }

    @Test
    fun `Should save and get dog lover avatar image`() {
        //given
        val dogLover = DogLoverData.adam
        val dogLoverId = dogLover.id
        val avatarMultipartFile = DogLoverAvatarImageData.avatarMultipartFile

        //when
        mockMvc.multipart("/profiles/avatar") {
            file(avatarMultipartFile)
            with {
                it.apply { method = "PUT" }
            }
        }
                //then
                .andExpect {
                    status { isOk }
                }

        //when
        val response = mockMvc.get("/profiles/$dogLoverId/avatar")
                //then
                .andExpect {
                    status { isOk }
                }
                .andReturn()
                .response
                .contentAsByteArray

        response contentEquals DogLoverAvatarImageData.avatarBytes shouldBe true
    }

    @Test
    fun `Should throw when adding invalid dog lover avatar image`() {
        //given
        val invalidAvatarMultipartFile = DogLoverAvatarImageData.invalidAvatarMultipartFile

        //when
        mockMvc.multipart("/profiles/avatar") {
            file(invalidAvatarMultipartFile)
            with {
                it.apply { method = "PUT" }
            }
        }
                //then
                .andExpect {
                    status {
                        isBadRequest
                        reason(InvalidAvatarImageException().message)
                    }
                }
    }
}