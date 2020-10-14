package pl.poznan.put.authservice.keycloak

import org.keycloak.representations.idm.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import pl.poznan.put.authservice.user.dto.UserDTO
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status.CREATED

@Service
class KeycloakService(
        private val keycloakClient: KeycloakClient
) {
    fun createUser(userDTO: UserDTO): ResponseEntity<String> {
        val userRepresentation = userDTO.toUserRepresentation()
        val response: Response = keycloakClient.createUser(userRepresentation)

        if (response.statusInfo.toEnum() == CREATED) {
            val userId = response.location.path.replace(".*/([^/]+)$".toRegex(), "$1")
            keycloakClient.setUserPassword(userId, userDTO.password)
//            keycloakClient.setUserRole(userId)
        }

        return ResponseEntity
                .status(response.status)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response.readEntity(String::class.java))
    }

    private fun UserDTO.toUserRepresentation() = UserRepresentation().apply {
        email = this@toUserRepresentation.email
        username = this@toUserRepresentation.email
        firstName = this@toUserRepresentation.firstName
        lastName = this@toUserRepresentation.lastName
    }
}