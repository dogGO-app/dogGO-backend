package pl.poznan.put.mailservice.modules.mail

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.mailservice.modules.mail.message.AccountActivationEmail

@RestController
class MailController(
        private val mailService: MailService
) {
    @ApiResponses(
            ApiResponse(description = "Sent successfully.", responseCode = "200"),
            ApiResponse(description = "Invalid email address.", responseCode = "400"),
            ApiResponse(description = "Sending mail failed.", responseCode = "500"))
    @PostMapping("/account-activation")
    fun sendAccountActivationMail(@RequestParam receiver: String, @RequestParam activationCode: String) {
        mailService.sendEmail(AccountActivationEmail(receiver, activationCode))
    }
}