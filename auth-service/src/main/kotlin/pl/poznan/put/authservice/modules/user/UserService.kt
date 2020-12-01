package pl.poznan.put.authservice.modules.user

import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.infrastructure.client.MailServiceClient
import pl.poznan.put.authservice.infrastructure.exceptions.InvalidActivationCodeException
import pl.poznan.put.authservice.infrastructure.exceptions.UserEmailAlreadyVerifiedException
import pl.poznan.put.authservice.infrastructure.extensions.getCurrentJwtTokenValue
import pl.poznan.put.authservice.modules.keycloak.KeycloakService
import pl.poznan.put.authservice.modules.user.cache.UserActivationCodeCache
import pl.poznan.put.authservice.modules.user.dto.UserActivationDTO
import pl.poznan.put.authservice.modules.user.dto.UserDTO

@Service
class UserService(
        private val keycloakService: KeycloakService,
        private val userActivationCodeCache: UserActivationCodeCache,
        private val mailServiceClient: MailServiceClient
) {
    fun createUser(userDTO: UserDTO): ResponseEntity<String> {
        val response = keycloakService.createUser(userDTO)
        if (response.statusCode == CREATED)
            sendUserActivationMailRequest(userDTO.email)

        return response
    }

    fun activateUser(userActivationDTO: UserActivationDTO) {
        val (userEmail, activationCode) = userActivationDTO
        if (!userActivationCodeCache.containsEntry(userEmail, activationCode))
            throw InvalidActivationCodeException()

        keycloakService.activateUser(userEmail)
    }

    fun sendUserActivationMail(userEmail: String) {
        if (keycloakService.isUserEmailVerified(userEmail))
            throw UserEmailAlreadyVerifiedException()

        sendUserActivationMailRequest(userEmail)
    }

    private fun sendUserActivationMailRequest(userEmail: String) {
        val activationCode = userActivationCodeCache.get(userEmail)
        mailServiceClient.sendUserActivationMail("Bearer ${getCurrentJwtTokenValue()}", userEmail, activationCode)
    }
}