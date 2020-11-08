package pl.poznan.put.mailservice.modules.mail

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MailController(
        private val mailService: MailService
) {
    @PostMapping("/account-activation")
    fun sendAccountActivationMail(@RequestParam receiver: String, @RequestParam activationCode: String) {
        mailService.sendAccountActivationMail(receiver, activationCode)
    }
}