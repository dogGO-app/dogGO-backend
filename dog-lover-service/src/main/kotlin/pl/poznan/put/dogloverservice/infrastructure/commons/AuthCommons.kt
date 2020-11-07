package pl.poznan.put.dogloverservice.infrastructure.commons

import java.util.UUID
import org.springframework.security.core.context.SecurityContextHolder

object AuthCommons {

    fun getCurrentUserId(): UUID {
        return UUID.fromString(SecurityContextHolder.getContext().authentication.name)
    }
}