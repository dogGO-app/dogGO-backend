package pl.poznan.put.mailservice.mail

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MailController(
        private val mailService: MailService
) {

    @GetMapping("/account-activation")
    fun sendAccountActivationMail(@RequestParam receiver: String, @RequestParam activationCode: String) {
        mailService.sendAccountActivationMail(receiver, activationCode)
    }

    @PreAuthorize("hasAuthority('SCOPE_TEST')")
    @GetMapping("/test")
    fun test(): String {
        return "Hello World!"
    }
}