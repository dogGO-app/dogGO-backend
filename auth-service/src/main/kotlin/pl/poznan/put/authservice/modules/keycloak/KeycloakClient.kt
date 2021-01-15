package pl.poznan.put.authservice.modules.keycloak

import java.util.UUID
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
import pl.poznan.put.authservice.infrastructure.exceptions.UserWithGivenEmailNotFoundException
import javax.ws.rs.core.Response

@Component
final class KeycloakClient(
        @Value("\${keycloak.custom.admin-user.username}") private val username: String,
        @Value("\${keycloak.custom.admin-user.password}") private val password: String,
        @Value("\${keycloak.custom.client.secret}") private val clientSecret: String,
        @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}") private val issuerUri: String
) {
    private companion object {
        const val CLIENT_ID = "admin-cli"
        const val MASTER_REALM_NAME = "master"
    }

    private val client: Keycloak = KeycloakBuilder.builder()
            .serverUrl(issuerUri.removeSuffix("/realms/$MASTER_REALM_NAME"))
            .realm(MASTER_REALM_NAME)
            .username(username)
            .password(password)
            .clientId(CLIENT_ID)
            .clientSecret(clientSecret)
            .resteasyClient(ResteasyClientBuilder().connectionPoolSize(10).build())
            .build()

    fun createUser(userRepresentation: UserRepresentation): Response =
            getUsersResource().create(userRepresentation)

    fun setUserPassword(userId: String, password: String) {
        val passwordCredential = createPasswordCredential(password)
        getUserResource(userId).resetPassword(passwordCredential)
    }

    fun setUserStatus(userEmail: String, enabled: Boolean, emailVerified: Boolean) {
        val userRepresentation = getUserByEmail(userEmail)
                ?: throw UserWithGivenEmailNotFoundException(userEmail)

        val userResource = getUserResource(userRepresentation.id)
        userResource.update(userRepresentation.apply {
            isEnabled = enabled
            isEmailVerified = emailVerified
        })
    }

    fun getUserByEmail(email: String): UserRepresentation? =
            getUsersResource().search(email, 0, 1).firstOrNull()

    fun getUserEmail(id: UUID): String =
            getUserResource(id.toString()).toRepresentation().email

    private fun getMasterRealmResource(): RealmResource =
            client.realm(MASTER_REALM_NAME)

    private fun getUsersResource(): UsersResource =
            getMasterRealmResource().users()

    private fun getUserResource(id: String): UserResource =
            getUsersResource().get(id)

    private fun createPasswordCredential(password: String) =
            CredentialRepresentation().apply {
                isTemporary = false
                type = CredentialRepresentation.PASSWORD
                value = password
            }
}