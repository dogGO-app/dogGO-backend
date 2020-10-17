package pl.poznan.put.dogloverservice.common

import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt

fun getUsername(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    return (authentication.principal as Jwt).claims["email"] as String
}

fun getUserId(): UUID {
    return UUID.fromString(SecurityContextHolder.getContext().authentication.name)
}