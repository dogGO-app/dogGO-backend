package pl.poznan.put.authservice.modules.user

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.ninjasquad.springmockk.MockkBean
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import io.mockk.every
import io.mockk.mockkStatic
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import pl.poznan.put.authservice.infrastructure.client.MailServiceClient
import pl.poznan.put.authservice.infrastructure.extensions.getCurrentJwtTokenValue
import pl.poznan.put.authservice.modules.keycloak.KeycloakClient
import pl.poznan.put.authservice.modules.user.cache.UserActivationCodeCache
import java.net.URI
import java.util.*
import javax.ws.rs.core.Response

@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_user"])
@AutoConfigureMockMvc
class UserControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {
    override fun beforeSpec(spec: Spec) {
        mockkStatic("pl.poznan.put.authservice.infrastructure.extensions.JwtExtensionsKt")
        every {
            getCurrentJwtTokenValue()
        } returns "token"
    }

    override fun listeners() = listOf(SpringListener)

    @MockkBean(relaxed = true)
    lateinit var keycloakClient: KeycloakClient

    @MockkBean(relaxed = true)
    lateinit var mailServiceClient: MailServiceClient

    @MockkBean
    lateinit var userActivationCodeCache: UserActivationCodeCache

    @Test
    fun `creating user with correct email and password should return created status`() {
        // Given
        val userDTO = UserData.userDTO
        val keycloakResponse = Response
                .status(Response.Status.CREATED)
                .location(URI("uri"))
                .entity("body")
                .build()

        every {
            keycloakClient.createUser(any())
        } returns keycloakResponse
        every {
            userActivationCodeCache.get(userDTO.email)
        } returns "111111"

        // When
        mockMvc.post("/users/sign-up") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(userDTO)
        }
                // Then
                .andExpect {
                    status { isCreated }
                }
    }

    @Test
    fun `creating user with incorrect email and correct password should return bad request status`() {
        // Given
        val userDTO = UserData.userDTO.copy(
                email = "incorrect"
        )

        // When
        mockMvc.post("/users/sign-up") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(userDTO)
        }
                // Then
                .andExpect {
                    status { isBadRequest }
                }
    }

    @Test
    fun `creating user with correct email and incorrect password should return bad request status`() {
        // Given
        val userDTO = UserData.userDTO.copy(
                password = "incorrect"
        )

        // When
        mockMvc.post("/users/sign-up") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(userDTO)
        }
                // Then
                .andExpect {
                    status { isBadRequest }
                }
    }

    @Test
    fun `activating user should return ok status`() {
        // Given
        val userActivationDTO = UserData.userActivationDTO

        every {
            userActivationCodeCache.containsEntry(userActivationDTO.email, userActivationDTO.activationCode)
        } returns true

        // When
        mockMvc.put("/users/activate") {
            contentType = MediaType.APPLICATION_JSON
            content = jacksonObjectMapper().writeValueAsString(userActivationDTO)
        }
                // Then
                .andExpect {
                    status { isOk }
                }
    }

    @Test
    fun `resending user activation mail should return ok status`() {
        // Given
        val userEmail = "test@test.pl"

        every {
            keycloakClient.getUserByEmail(userEmail)
        } returns UserRepresentation().apply {
            email = userEmail
            isEmailVerified = false
        }
        every {
            userActivationCodeCache.get(userEmail)
        } returns "111111"

        // When
        mockMvc.post("/users/resend-activation-mail") {
            param("userEmail", userEmail)
        }
                // Then
                .andExpect {
                    status { isOk }
                }
    }

    @Test
    fun `resending already activated user activation mail should return conflict status`() {
        // Given
        val userEmail = "test@test.pl"

        every {
            keycloakClient.getUserByEmail(userEmail)
        } returns UserRepresentation().apply {
            email = userEmail
            isEmailVerified = true
        }

        // When
        mockMvc.post("/users/resend-activation-mail") {
            param("userEmail", userEmail)
        }
                // Then
                .andExpect {
                    status { isConflict }
                }
    }

    @Test
    fun `getting users emails should return ok status and users emails`() {
        // Given
        val userIds = arrayOf(
                "fa17a243-08e7-4921-90cd-8c7612c01002",
                "84a36eb7-72a5-4e3f-a84a-8bfcc7e1412f",
                "1bb345f9-7e9b-4e43-8f32-08032a334c63"
        )
        val expected = UserData.userEmails

        userIds.forEach {
            val userId = UUID.fromString(it)
            every {
                keycloakClient.getUserEmail(userId)
            } returns UserData.userEmails[userId]!!
        }

        // When
        val result = mockMvc.get("/users/emails") {
            param("usersIds", *userIds)
        }
                // Then
                .andExpect {
                    status { isOk }
                }
                .andReturn()


        val actual = jacksonObjectMapper().readValue<Map<UUID, String>>(result.response.contentAsString)
        actual shouldBe expected
    }
}