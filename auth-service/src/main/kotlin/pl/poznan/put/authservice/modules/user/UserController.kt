package pl.poznan.put.authservice.modules.user

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.poznan.put.authservice.modules.user.dto.UserActivationDTO
import pl.poznan.put.authservice.modules.user.dto.UserDTO
import javax.validation.Valid

@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserService
) {
    @ApiResponses(
            ApiResponse(description = "User created.", responseCode = "201"),
            ApiResponse(description = "User email is invalid.", responseCode = "400"),
            ApiResponse(description = "User exists with same username.", responseCode = "409"))
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@Valid @RequestBody userDTO: UserDTO): ResponseEntity<String> {
        return userService.createUser(userDTO)
    }

    @ApiResponses(
            ApiResponse(description = "User account activated.", responseCode = "200"),
            ApiResponse(description = "Invalid activation code.", responseCode = "400"),
            ApiResponse(description = "User doesn't exist.", responseCode = "404"))
    @PutMapping("/activate")
    fun activateUser(@RequestBody userActivationDTO: UserActivationDTO) {
        userService.activateUser(userActivationDTO)
    }

    @ApiResponses(
            ApiResponse(description = "Sent successfully.", responseCode = "200"),
            ApiResponse(description = "User doesn't exist.", responseCode = "404"),
            ApiResponse(description = "User email is already verified.", responseCode = "409"))
    @PostMapping("/send-activation-mail")
    fun sendUserActivationMail(@RequestParam userEmail: String) {
        userService.sendUserActivationMail(userEmail)
    }
}