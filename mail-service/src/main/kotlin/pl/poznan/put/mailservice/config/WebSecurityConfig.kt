package pl.poznan.put.mailservice.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests().anyRequest().hasAuthority("SCOPE_user")
//                .authorizeRequests().anyRequest().authenticated() instead of above works too
                .and()
                .oauth2ResourceServer().jwt()
    }
}