package pl.poznan.put.mailservice.mail

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MailController (
        private val mailService: MailService
) {

    @GetMapping("/account-activation")
    fun sendAccountActivationMail(@RequestParam receiver: String, @RequestParam activationCode: String): ResponseEntity<*> {
        return ResponseEntity(mailService.sendAccountActivationMail(receiver, activationCode), HttpStatus.OK)
    }
}