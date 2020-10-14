package pl.poznan.put.authservice.user.dto

data class UserDTO(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String
)