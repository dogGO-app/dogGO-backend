package pl.poznan.put.mailservice.infrastructure.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/v3/api-docs/**").permitAll()
                .anyRequest().hasAuthority("SCOPE_user")
                .and()
                .csrf().disable()
                .oauth2ResourceServer().jwt()
    }
}