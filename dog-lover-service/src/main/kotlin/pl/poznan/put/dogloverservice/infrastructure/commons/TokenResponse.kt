package pl.poznan.put.dogloverservice.infrastructure.commons

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenResponse (

    @JsonProperty("access_token")
    val accessToken: String?,

    @JsonProperty("refresh_token")
    val refreshToken: String?,

    @JsonProperty("expires_in")
    val expiresIn: Int?,

    @JsonProperty("scope")
    val scope: String?

)