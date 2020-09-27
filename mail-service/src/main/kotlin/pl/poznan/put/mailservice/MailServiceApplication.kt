package pl.poznan.put.mailservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MailServiceApplication

fun main(args: Array<String>) {
	runApplication<MailServiceApplication>(*args)
}
