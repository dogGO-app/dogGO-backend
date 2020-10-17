package pl.poznan.put.authservice.modules.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.modules.keycloak.KeycloakService
import pl.poznan.put.authservice.modules.user.cache.UserActivationCodeCache
import pl.poznan.put.authservice.modules.user.dto.UserDTO

@Service
class UserService(
        private val keycloakService: KeycloakService,
        private val userActivationCodeCache: UserActivationCodeCache,
) {
    fun createUser(userDTO: UserDTO): ResponseEntity<String> {
        return keycloakService.createUser(userDTO)
    }

    fun sendUserActivationMail(userEmail: String) {
        when (keycloakService.isUserEmailVerified(userEmail)) {
           true -> throw IllegalStateException("User email is already verified!")
           null -> throw IllegalStateException("User with given email doesn't exist!")
        }

        val activationCode = userActivationCodeCache.get(userEmail)
        // TODO Feign client
    }

    fun activateUser(userEmail: String, activationCode: String) {
        if (!userActivationCodeCache.containsEntry(userEmail, activationCode))
            throw IllegalStateException("Invalid activation code!")

        keycloakService.activateUser(userEmail)
    }
}