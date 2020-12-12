package pl.poznan.put.dogloverservice.infrastructure.client

import java.util.UUID
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "auth-service")
interface AuthServiceClient {

    @GetMapping("/users/emails")
    fun getUsersEmails(@RequestHeader(value = "Authorization") token: String,
                       @RequestParam usersIds: List<UUID>
    ): Map<UUID, String>
}