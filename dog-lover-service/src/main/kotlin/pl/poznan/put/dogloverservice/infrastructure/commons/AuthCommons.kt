package pl.poznan.put.dogloverservice.infrastructure.commons

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import java.util.*

object AuthCommons {
    fun getCurrentUsername(): String {
        val authentication = SecurityContextHolder.getContext().authentication
        return (authentication.principal as Jwt).claims["email"] as String
    }

    fun getCurrentUserId(): UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name)
    }
}