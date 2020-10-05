package pl.poznan.put.authservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

// TODO: Remove this controller
@RestController
class TestController {
    @GetMapping("/test")
    fun test(): String {
        return "Hello World!"
    }
}