package pl.poznan.put.authservice.user

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.keycloak.KeycloakService
import pl.poznan.put.authservice.user.dto.UserDTO

@Service
class UserService(
        private val keycloakService: KeycloakService
) {
    fun createUser(userDTO: UserDTO): ResponseEntity<String> {
        return keycloakService.createUser(userDTO)
    }
}