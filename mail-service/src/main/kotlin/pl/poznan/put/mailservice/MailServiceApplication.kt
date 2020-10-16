package pl.poznan.put.mailservice

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = Info(title = "Mail API", version = "1.0", description = "Documentation Mail API v1.0"))
class MailServiceApplication

fun main(args: Array<String>) {
    runApplication<MailServiceApplication>(*args)
}
