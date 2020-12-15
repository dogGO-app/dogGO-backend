package pl.poznan.put.authservice.modules.user

import pl.poznan.put.authservice.modules.user.dto.UserActivationDTO
import pl.poznan.put.authservice.modules.user.dto.UserDTO
import java.util.*

object UserData {
    val userDTO
        get() = UserDTO(
                email = "test@test.pl",
                password = "Testing12345"
        )

    val userActivationDTO
        get() = UserActivationDTO(
                email = "test@test.pl",
                activationCode = "111111"
        )

    val userIds
        get() = listOf(
                UUID.fromString("fa17a243-08e7-4921-90cd-8c7612c01002"),
                UUID.fromString("84a36eb7-72a5-4e3f-a84a-8bfcc7e1412f"),
                UUID.fromString("1bb345f9-7e9b-4e43-8f32-08032a334c63")
        )

    val userEmails
        get() = mapOf(
             userIds[0] to "test1@test.pl",
             userIds[1] to "test2@test.pl",
             userIds[2] to "test3@test.pl",
        )
}