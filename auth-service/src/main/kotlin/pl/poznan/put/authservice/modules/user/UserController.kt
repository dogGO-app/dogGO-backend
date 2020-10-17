package pl.poznan.put.authservice.modules.user

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import pl.poznan.put.authservice.modules.user.dto.UserDTO

@RestController
@RequestMapping("/user")
class UserController(
        private val userService: UserService
) {
    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    fun createUser(@RequestBody userDTO: UserDTO): ResponseEntity<String> {
        return userService.createUser(userDTO)
    }
}