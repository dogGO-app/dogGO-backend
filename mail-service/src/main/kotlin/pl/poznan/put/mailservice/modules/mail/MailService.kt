package pl.poznan.put.mailservice.modules.mail

import mu.KotlinLogging
import org.apache.commons.validator.routines.EmailValidator
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine
import pl.poznan.put.mailservice.infrastructure.exceptions.InvalidEmailException
import pl.poznan.put.mailservice.infrastructure.exceptions.MailSendException
import pl.poznan.put.mailservice.modules.mail.message.EmailMessage
import javax.mail.internet.MimeMessage

private val logger = KotlinLogging.logger {}

@Service
class MailService(
        private val javaMailSender: JavaMailSender,
        private val templateEngine: SpringTemplateEngine,
        @Value("\${spring.mail.username}") private val senderEmail: String
) {
    fun sendEmail(emailMessage: EmailMessage) {
        val receiver = emailMessage.receiverEmail
        validateEmail(receiver)
        try {
            val message = createMimeMessage(emailMessage)
            logger.info { "Sending mail to $receiver..." }
            javaMailSender.send(message)
            logger.info { "Mail to $receiver sent successfully" }
        } catch (e: MailException) {
            logger.error(e) { "Sending mail to $receiver failed" }
            throw MailSendException(receiver)
        }
    }

    private fun validateEmail(email: String) {
        val validator = EmailValidator.getInstance()
        if (!validator.isValid(email))
            throw InvalidEmailException(email)
    }

    private fun createMimeMessage(emailMessage: EmailMessage): MimeMessage {
        val createMailContent = {
            val context = Context().apply {
                emailMessage.variables.forEach { (name, value) ->
                    setVariable(name, value)
                }
            }
            templateEngine.process(emailMessage.templateName, context)
        }

        return javaMailSender.createMimeMessage().apply {
            MimeMessageHelper(this, true).apply {
                setFrom(senderEmail)
                setTo(emailMessage.receiverEmail)
                setSubject(emailMessage.subject)
                setText(createMailContent(), true)
            }
        }
    }
}
