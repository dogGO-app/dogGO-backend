package pl.poznan.put.authservice.modules.keycloak

import java.util.UUID
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.infrastructure.exceptions.UserWithGivenEmailNotFoundException
import pl.poznan.put.authservice.modules.user.dto.UserDTO
import javax.ws.rs.core.Response.Status.CREATED

@Service
class KeycloakService(
        private val keycloakClient: KeycloakClient
) {
    fun createUser(userDTO: UserDTO): ResponseEntity<String> {
        val userRepresentation = userDTO.toUserRepresentation()
        val response = keycloakClient.createUser(userRepresentation)

        if (response.statusInfo.toEnum() == CREATED) {
            val userId = response.location.path.replace(".*/([^/]+)$".toRegex(), "$1")
            keycloakClient.setUserPassword(userId, userDTO.password)
        }

        return ResponseEntity
                .status(response.status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.readEntity(String::class.java))
    }

    fun activateUser(userEmail: String) {
        keycloakClient.setUserStatus(userEmail, enabled = true, emailVerified = true)
    }

    fun isUserEmailVerified(userEmail: String): Boolean {
        val user = keycloakClient.getUserByEmail(userEmail)
                ?: throw UserWithGivenEmailNotFoundException(userEmail)
        return user.isEmailVerified
    }

    fun getUsersEmails(ids: List<UUID>): Map<UUID, String> {
        return ids.associateWith { keycloakClient.getUserEmail(it) }
    }

    private fun UserDTO.toUserRepresentation() = UserRepresentation().apply {
        email = this@toUserRepresentation.email
        username = this@toUserRepresentation.email
        isEnabled = false
    }
}