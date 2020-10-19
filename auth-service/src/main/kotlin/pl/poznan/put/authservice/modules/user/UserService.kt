package pl.poznan.put.authservice.modules.user

import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.infrastructure.client.MailServiceClient
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
        return keycloakService.createUser(userDTO)
    }

    fun activateUser(userActivationDTO: UserActivationDTO) {
        val (userEmail, activationCode) = userActivationDTO
        if (!userActivationCodeCache.containsEntry(userEmail, activationCode))
            // TODO: Use ResponseStatusExceptions in whole project
            throw IllegalStateException("Invalid activation code!")

        keycloakService.activateUser(userEmail)
    }

    fun sendUserActivationMail(userEmail: String) {
        when (keycloakService.isUserEmailVerified(userEmail)) {
            true -> throw IllegalStateException("User email is already verified!")
            null -> throw IllegalStateException("User with given email doesn't exist!")
        }

        val activationCode = userActivationCodeCache.get(userEmail)
        val jwt = SecurityContextHolder.getContext().authentication.principal as Jwt
        mailServiceClient.sendUserActivationMail("Bearer ${jwt.tokenValue}", userEmail, activationCode)
    }
}