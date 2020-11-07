package pl.poznan.put.authservice.modules.user.dto

import javax.validation.constraints.Email
import javax.validation.constraints.Size

data class UserDTO(

        @field:Email
        @field:Size(max = 50)
        val email: String,

        @field:Size(min = 8, max = 120)
        val password: String,
)