package pl.poznan.put.mailservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient

@SpringBootApplication
@EnableDiscoveryClient
class MailServiceApplication

fun main(args: Array<String>) {
	runApplication<MailServiceApplication>(*args)
}
