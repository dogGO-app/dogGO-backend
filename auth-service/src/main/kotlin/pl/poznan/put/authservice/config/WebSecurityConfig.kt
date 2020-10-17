package pl.poznan.put.authservice.config

import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(http: HttpSecurity) {
        http
                .authorizeRequests()
                .antMatchers("/user/secured").authenticated()
                .and()
                .oauth2ResourceServer().jwt()
    }

    override fun configure(web: WebSecurity) {
        web.ignoring().antMatchers("/keycloak/**")
    }
}