package pl.poznan.put.authservice.infrastructure.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "mail-service")
interface MailServiceClient {
    @PostMapping("/account-activation")
    fun sendUserActivationMail(
            @RequestHeader(value = "Authorization") token: String,
            @RequestParam receiver: String,
            @RequestParam activationCode: String
    )
}