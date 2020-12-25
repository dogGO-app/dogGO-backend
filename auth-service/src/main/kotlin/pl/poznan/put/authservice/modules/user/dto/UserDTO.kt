package pl.poznan.put.authservice.modules.user.dto

import pl.poznan.put.authservice.validation.constraints.Password
import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class UserDTO(

        @field:Email
        @field:Size(max = 64)
        val email: String,

        @field:Password
        val password: String
)