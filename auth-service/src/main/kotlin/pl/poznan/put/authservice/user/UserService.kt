package pl.poznan.put.authservice.user

import org.springframework.stereotype.Service
import pl.poznan.put.authservice.keycloak.KeycloakClient
import pl.poznan.put.authservice.user.dto.UserDTO

@Service
class UserService(
        private val keycloakClient: KeycloakClient
) {
    fun createUser(userDTO: UserDTO) {
        keycloakClient.createUser(userDTO)
    }
}