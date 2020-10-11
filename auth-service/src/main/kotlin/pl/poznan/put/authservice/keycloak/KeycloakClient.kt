package pl.poznan.put.authservice.keycloak

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.admin.client.resource.RealmResource
import org.keycloak.admin.client.resource.UserResource
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import pl.poznan.put.authservice.user.dto.UserDTO
import javax.ws.rs.core.Response

@Component
final class KeycloakClient(
        @Value("\${keycloak.custom.admin-user.username}") val username: String,
        @Value("\${keycloak.custom.admin-user.password}") val password: String
) {
    companion object {
        const val SERVER_URL = "http://localhost:8888/auth"
        const val CLIENT_ID = "admin-cli"
        const val CLIENT_SECRET = "3adf256f-b398-4acb-a7dc-188d71e1ff90"
        const val MASTER_REALM_NAME = "master"
        const val USER_ROLE = "user"
    }

    private val client: Keycloak = KeycloakBuilder.builder()
            .serverUrl(SERVER_URL)
            .realm(MASTER_REALM_NAME)
            .username(username)
            .password(password)
            .clientId(CLIENT_ID)
            .clientSecret(CLIENT_SECRET)
            .resteasyClient(ResteasyClientBuilder().connectionPoolSize(20).build())
            .build()

    fun createUser(userDTO: UserDTO): Response.Status {
        val userRepresentation = userDTO.toUserRepresentation().apply {
            isEnabled = true
        }
        val result = getUsers().create(userRepresentation)

        return when (val responseStatus = result.statusInfo.toEnum()) {
            Response.Status.CREATED -> {
                val userId = result.location.path.replace(".*/([^/]+)$".toRegex(), "$1")

                setUserPassword(userId, userDTO.password)
                setUserRole(userId)

                responseStatus
            }
            else -> responseStatus
        }
    }

    private fun getMasterRealm(): RealmResource =
            client.realm(MASTER_REALM_NAME)

    private fun getUsers(): UsersResource =
            getMasterRealm().users()

    private fun getUser(id: String): UserResource =
            getUsers().get(id)

    private fun setUserPassword(userId: String, password: String) {
        val passwordCredential = createPasswordCredential(password)
        getUser(userId).resetPassword(passwordCredential)
    }

    private fun createPasswordCredential(password: String) =
            CredentialRepresentation().apply {
                isTemporary = false
                type = CredentialRepresentation.PASSWORD
                value = password
            }

    private fun setUserRole(userId: String) {
        val userRoleRepresentation = getMasterRealm().roles().get(USER_ROLE).toRepresentation()
        getUser(userId).roles().realmLevel().add(listOf(userRoleRepresentation))
    }

    private fun UserDTO.toUserRepresentation() = UserRepresentation().apply {
        email = this@toUserRepresentation.email
        username = this@toUserRepresentation.username
        firstName = this@toUserRepresentation.firstName
        lastName = this@toUserRepresentation.lastName
    }
}