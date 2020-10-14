package pl.poznan.put.gatewayservice.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain =
            http
                    .authorizeExchange()
                    .anyExchange().hasAuthority("SCOPE_user")
//                    .anyExchange().authenticated()
                    .and()
                    .csrf().disable()
                    .build()
}