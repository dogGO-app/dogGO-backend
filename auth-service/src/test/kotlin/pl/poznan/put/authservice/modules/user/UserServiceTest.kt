package pl.poznan.put.authservice.modules.user

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import pl.poznan.put.authservice.infrastructure.client.MailServiceClient
import pl.poznan.put.authservice.infrastructure.exceptions.InvalidActivationCodeException
import pl.poznan.put.authservice.infrastructure.exceptions.UserEmailAlreadyVerifiedException
import pl.poznan.put.authservice.infrastructure.extensions.getCurrentJwtTokenValue
import pl.poznan.put.authservice.modules.keycloak.KeycloakService
import pl.poznan.put.authservice.modules.user.cache.UserActivationCodeCache

class UserServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    mockkStatic("pl.poznan.put.authservice.infrastructure.extensions.JwtExtensionsKt")
    val keycloakService = mockk<KeycloakService>(relaxed = true)
    val userActivationCodeCache = mockk<UserActivationCodeCache>(relaxed = true)
    val mailServiceClient = mockk<MailServiceClient>(relaxed = true)

    val userService = UserService(
            keycloakService,
            userActivationCodeCache,
            mailServiceClient
    )

    val token = "token"
    every {
        getCurrentJwtTokenValue()
    } returns token

    Given("user DTO") {
        val userDTO = UserData.userDTO

        And("user with given email doesn't already exist") {
            val response = ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body("body")

            every {
                keycloakService.createUser(userDTO)
            } returns response
            val activationCode = "111111"
            every {
                userActivationCodeCache.get(userDTO.email)
            } returns activationCode

            When("creating user") {
                val actualResponse = userService.createUser(userDTO)

                Then("actual response should be equal to expected") {
                    actualResponse shouldBe response
                }

                Then("userActivationCodeCache and mailServiceClient should be called") {
                    verifyOrder {
                        userActivationCodeCache.get(userDTO.email)
                        mailServiceClient.sendUserActivationMail("Bearer $token", userDTO.email, activationCode)
                    }
                }
            }
        }

        And("user with given email already exists") {
            val response = ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("body")

            every {
                keycloakService.createUser(userDTO)
            } returns response

            When("creating user") {
                val actualResponse = userService.createUser(userDTO)

                Then("actual response should be equal to expected") {
                    actualResponse shouldBe response
                }

                Then("userActivationCodeCache and mailServiceClient should not be called") {
                    verify(exactly = 0) {
                        userActivationCodeCache.get(any())
                        mailServiceClient.sendUserActivationMail(any(), any(), any())
                    }
                }
            }
        }
    }

    Given("user activation DTO") {
        val userActivationDTO = UserData.userActivationDTO

        And("user activation code cache contains same entry as DTO") {
            every {
                userActivationCodeCache.containsEntry(userActivationDTO.email, userActivationDTO.activationCode)
            } returns true

            When("activating user") {
                userService.activateUser(userActivationDTO)

                Then("keycloakService should be called") {
                    verify {
                        keycloakService.activateUser(userActivationDTO.email)
                    }
                }
            }
        }

        And("user activation code cache doesn't contain same entry as DTO") {
            every {
                userActivationCodeCache.containsEntry(userActivationDTO.email, userActivationDTO.activationCode)
            } returns false

            When("activating user") {
                val exception = shouldThrow<InvalidActivationCodeException> {
                    userService.activateUser(userActivationDTO)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage InvalidActivationCodeException().message
                }

                Then("keycloakService should not be called") {
                    verify(exactly = 0) {
                        keycloakService.activateUser(userActivationDTO.email)
                    }
                }
            }
        }
    }

    Given("user email") {
        val userEmail = "test@test.pl"

        And("user email is not already verified") {
            every {
                keycloakService.isUserEmailVerified(userEmail)
            } returns false

            val activationCode = "111111"
            every {
                userActivationCodeCache.get(userEmail)
            } returns activationCode

            When("resending user activation mail") {
                userService.resendUserActivationMail(userEmail)

                Then("userActivationCodeCache and mailServiceClient should be called") {
                    verifyOrder {
                        userActivationCodeCache.get(userEmail)
                        mailServiceClient.sendUserActivationMail("Bearer $token", userEmail, activationCode)
                    }
                }
            }
        }

        And("user email is already verified") {
            every {
                keycloakService.isUserEmailVerified(userEmail)
            } returns true

            When("resending user activation mail") {
                val exception = shouldThrow<UserEmailAlreadyVerifiedException> {
                    userService.resendUserActivationMail(userEmail)
                }

                Then("exception should have correct message") {
                    exception shouldHaveMessage UserEmailAlreadyVerifiedException().message
                }

                Then("userActivationCodeCache and mailServiceClient should not be called") {
                    verify(exactly = 0) {
                        userActivationCodeCache.get(any())
                        mailServiceClient.sendUserActivationMail(any(), any(), any())
                    }
                }
            }
        }
    }

    Given("user IDs") {
        val userIds = UserData.userIds
        val userEmails = UserData.userEmails

        every {
            keycloakService.getUsersEmails(userIds)
        } returns userEmails

        When("getting users' emails") {
            val actualUsersEmails = userService.getUsersEmails(userIds)

            Then("actual users' emails should be equal to expected") {
                actualUsersEmails shouldBe userEmails
            }
        }
    }
})