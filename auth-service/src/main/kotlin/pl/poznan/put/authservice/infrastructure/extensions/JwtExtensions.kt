package pl.poznan.put.authservice.infrastructure.extensions

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

fun getCurrentJwtTokenValue(): String =
        (SecurityContextHolder.getContext().authentication as Jwt).tokenValue