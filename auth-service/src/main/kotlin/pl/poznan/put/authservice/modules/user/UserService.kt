package pl.poznan.put.authservice.modules.user

import java.util.UUID
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
            sendUserActivationMail(userDTO.email)

        return response
    }

    fun activateUser(userActivationDTO: UserActivationDTO) {
        val (userEmail, activationCode) = userActivationDTO
        if (!userActivationCodeCache.containsEntry(userEmail, activationCode))
            throw InvalidActivationCodeException()

        keycloakService.activateUser(userEmail)
    }

    fun resendUserActivationMail(userEmail: String) {
        if (keycloakService.isUserEmailVerified(userEmail))
            throw UserEmailAlreadyVerifiedException()

        sendUserActivationMail(userEmail)
    }

    fun getUsersEmails(usersIds: List<UUID>) =
            keycloakService.getUsersEmails(usersIds)

    private fun sendUserActivationMail(userEmail: String) {
        val activationCode = userActivationCodeCache.get(userEmail)
        mailServiceClient.sendUserActivationMail("Bearer ${getCurrentJwtTokenValue()}", userEmail, activationCode)
    }
}