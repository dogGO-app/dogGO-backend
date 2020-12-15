package pl.poznan.put.authservice.modules.keycloak

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import pl.poznan.put.authservice.infrastructure.exceptions.UserWithGivenEmailNotFoundException
import pl.poznan.put.authservice.modules.user.UserData
import java.net.URI
import javax.ws.rs.core.Response

class KeycloakServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val keycloakClient = mockk<KeycloakClient>(relaxed = true)

    val keycloakService = KeycloakService(keycloakClient)

    Given("user DTO") {
        val userDTO = UserData.userDTO

        And("user with given email doesn't already exist") {
            val keycloakResponse = Response
                    .status(Response.Status.CREATED)
                    .location(URI("uri"))
                    .entity("body")
                    .build()

            every {
                keycloakClient.createUser(any())
            } returns keycloakResponse

            val expectedResponse = ResponseEntity
                    .status(keycloakResponse.status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(keycloakResponse.readEntity(String::class.java))

            When("creating user") {
                val actualResponse = keycloakService.createUser(userDTO)

                Then("keycloakClient should be called") {
                    verify(exactly = 1) {
                        keycloakClient.setUserPassword(userId = any(), userDTO.password)
                    }
                }

                Then("actual response should be equal to expected") {
                    actualResponse shouldBe expectedResponse
                }
            }
        }

        And("user with given email already exists") {
            val keycloakResponse = Response
                    .status(Response.Status.CONFLICT)
                    .entity("body")
                    .build()

            every {
                keycloakClient.createUser(any())
            } returns keycloakResponse

            val expectedResponse = ResponseEntity
                    .status(keycloakResponse.status)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(keycloakResponse.readEntity(String::class.java))

            When("creating user") {
                val actualResponse = keycloakService.createUser(userDTO)

                Then("keycloakClient should not be called") {
                    verify(exactly = 0) {
                        keycloakClient.setUserPassword(userId = any(), password = any())
                    }
                }

                Then("actual response should be equal to expected") {
                    actualResponse shouldBe expectedResponse
                }
            }
        }
    }

    Given("user email") {
        val userEmail = "test@test.pl"

        When("activating user") {
            keycloakService.activateUser(userEmail)

            Then("keycloakClient should be invoked with correct parameters") {
                verify(exactly = 1) {
                    keycloakClient.setUserStatus(userEmail, enabled = true, emailVerified = true)
                }
            }
        }

        And("user with given email exists") {
            val userRepresentation = UserRepresentation().apply {
                email = userEmail
                isEmailVerified = true
            }

            every {
                keycloakClient.getUserByEmail(userEmail)
            } returns userRepresentation

            When("checking user email is verified") {
                val isUserEmailVerified = keycloakService.isUserEmailVerified(userEmail)

                Then("is email verified flag should be equal to expected") {
                    isUserEmailVerified shouldBe userRepresentation.isEmailVerified
                }
            }
        }

        And("user with given email doesn't exist") {
            every {
                keycloakClient.getUserByEmail(userEmail)
            } returns null

            When("checking user email is verified") {
                val exception = shouldThrow<UserWithGivenEmailNotFoundException> {
                    keycloakService.isUserEmailVerified(userEmail)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage UserWithGivenEmailNotFoundException(userEmail).message
                }
            }
        }
    }

    Given("user IDs") {
        val userIds = UserData.userIds
        val userEmails = UserData.userEmails

        userIds.forEach { userId ->
            every {
                keycloakClient.getUserEmail(userId)
            } returns userEmails[userId]!!
        }

        When("getting users' emails") {
            val actualUserEmails = keycloakService.getUsersEmails(userIds)

            Then("actual user emails should be equal to expected") {
                actualUserEmails shouldBe userEmails
            }
        }
    }
})