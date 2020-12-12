package pl.poznan.put.dogloverservice.infrastructure.commons

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.RestTemplate
import pl.poznan.put.dogloverservice.infrastructure.exceptions.AccessTokenException

@Component
class RestTemplateTokenRequester(
        @Value("\${self.token.url}") private val tokenUrl: String,
        @Value("\${self.token.client-id}") private val clientId: String,
        @Value("\${self.token.client-secret}") private val clientSecret: String
) {

    fun getAccessToken(): String {
        val headers = HttpHeaders().apply {
            contentType = MediaType.APPLICATION_FORM_URLENCODED }
        val requestBody = prepareRequestBody()
        val entity = HttpEntity(requestBody, headers)

        val response: ResponseEntity<TokenResponse> = RestTemplate().exchange(tokenUrl,
                HttpMethod.POST,
                entity,
                TokenResponse::class.java)
        return response.body?.accessToken ?: throw AccessTokenException()
    }

    private fun prepareRequestBody(): LinkedMultiValueMap<String, String> {
        val requestBody = LinkedMultiValueMap<String, String>()
        requestBody.add("client_id", clientId)
        requestBody.add("client_secret", clientSecret)
        requestBody.add("grant_type", "client_credentials")

        return requestBody
    }
}