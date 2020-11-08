package pl.poznan.put.mailservice.mail

import mu.KotlinLogging
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import javax.mail.internet.MimeMessage

private val logger = KotlinLogging.logger {}

@Service
class MailService(
        private val javaMailSender: JavaMailSender,
        private val templateEngine: SpringTemplateEngine,
        @Value("\${spring.mail.username}") private val senderEmail: String
) {

    companion object {
        const val ACCOUNT_ACTIVATION_SUBJECT = "Aktywacja konta na platformie dogGO"
    }

    fun sendAccountActivationMail(receiver: String, activationCode: String) {
        validateEmail(receiver)
        try {
            val message = prepareAccountActivationMessage(receiver, activationCode)
            logger.info { "Sending mail to $receiver..." }
            javaMailSender.send(message)
            logger.info { "Mail to $receiver sent successfully" }
        } catch (e: MailException) {
            val message = "Sending mail to $receiver failed"
            logger.error(message, e)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "$message.")
        }
    }

    private fun validateEmail(email: String) {
        val validator = EmailValidator.getInstance()
        if (!validator.isValid(email))
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email address: $email")
    }

    private fun prepareAccountActivationMessage(receiver: String, activationCode: String): MimeMessage {
        return javaMailSender.createMimeMessage().apply {
            MimeMessageHelper(this, true).apply {
                setFrom(senderEmail)
                setTo(receiver)
                setSubject(ACCOUNT_ACTIVATION_SUBJECT)
                setText(prepareAccountActivationContent(receiver, activationCode), true)
            }
        }
    }

    private fun prepareAccountActivationContent(receiver: String, activationCode: String): String {
        val context = Context().apply {
            setVariable("usermail", receiver.substringBefore("@"))
            setVariable("code", activationCode)
        }
        return templateEngine.process("account-activation", context)
    }
}
