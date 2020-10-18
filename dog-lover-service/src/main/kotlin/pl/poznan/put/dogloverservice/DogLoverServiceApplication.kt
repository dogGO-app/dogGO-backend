package pl.poznan.put.dogloverservice

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
@OpenAPIDefinition(info = Info(title = "Dog Lover API", version = "1.0", description = "Documentation Dog Lover API v1.0"))
class DogLoverServiceApplication

fun main(args: Array<String>) {
    runApplication<DogLoverServiceApplication>(*args)
}
