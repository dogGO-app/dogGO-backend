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
import javax.ws.rs.core.Response

@Component
final class KeycloakClient(
        @Value("\${keycloak.custom.admin-user.username}") private val username: String,
        @Value("\${keycloak.custom.admin-user.password}") private val password: String
) {
    private companion object {
        const val SERVER_URL = "http://localhost:8888/keycloak"
        const val CLIENT_ID = "admin-cli"
        const val CLIENT_SECRET = "043a5366-3120-4ec2-93d1-4d69120481c9"
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
            .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build())
            .build()

    fun createUser(userRepresentation: UserRepresentation): Response =
            getUsers().create(userRepresentation)

    fun setUserPassword(userId: String, password: String) {
        val passwordCredential = createPasswordCredential(password)
        getUser(userId).resetPassword(passwordCredential)
    }

    fun setUserRole(userId: String) {
        val userRoleRepresentation = getMasterRealm().roles().get(USER_ROLE).toRepresentation()
        getUser(userId).roles().realmLevel().add(listOf(userRoleRepresentation))
    }

    private fun getMasterRealm(): RealmResource =
            client.realm(MASTER_REALM_NAME)

    private fun getUsers(): UsersResource =
            getMasterRealm().users()

    private fun getUser(id: String): UserResource =
            getUsers().get(id)

    private fun createPasswordCredential(password: String) =
            CredentialRepresentation().apply {
                isTemporary = false
                type = CredentialRepresentation.PASSWORD
                value = password
            }
}