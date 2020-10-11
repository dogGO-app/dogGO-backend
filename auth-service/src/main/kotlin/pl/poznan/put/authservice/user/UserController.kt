package pl.poznan.put.authservice.user

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.authservice.user.dto.UserDTO

@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserService
) {
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(userDTO: UserDTO) {
        userService.createUser(userDTO)
    }
}