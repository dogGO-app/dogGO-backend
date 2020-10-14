package pl.poznan.put.gatewayservice

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.WebSession
import reactor.core.publisher.Mono

@RestController
class GatewayController {
    @GetMapping("/")
    fun index(session: WebSession): Mono<String> =
            Mono.just(session.id)

    @GetMapping("/token")
    fun getHome(@RegisteredOAuth2AuthorizedClient authorizedClient: OAuth2AuthorizedClient): Mono<String> =
            Mono.just(authorizedClient.accessToken.tokenValue)
}