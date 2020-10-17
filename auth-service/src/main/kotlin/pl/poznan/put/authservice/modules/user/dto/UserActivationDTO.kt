package pl.poznan.put.authservice.modules.user.dto

data class UserActivationDTO(
        val email: String,
        val activationCode: String
)