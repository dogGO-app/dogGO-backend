package pl.poznan.put.mailservice.mail

import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.context.Context
import org.thymeleaf.spring5.SpringTemplateEngine

@Service
class MailService(
        private val javaMailSender: JavaMailSender,
        private val templateEngine: SpringTemplateEngine,
        @Value("\${spring.mail.username}") private val senderEmail: String,
        @Value("\${account-activation.subject}") private val accountActivationSubject: String
) {

    fun sendAccountActivationMail(receiver: String, activationCode: String) {
        val message = javaMailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true)
        helper.setFrom(senderEmail)
        helper.setTo(receiver)
        helper.setSubject(accountActivationSubject)
        helper.setText(prepareAccountActivationContent(receiver, activationCode), true)
        javaMailSender.send(message)
    }

    private fun prepareAccountActivationContent(receiver: String, activationCode: String): String {
        val context = Context()
        context.setVariable("usermail", receiver.substringBefore("@"))
        context.setVariable("code", activationCode)
        return templateEngine.process("account-activation", context)
    }
}
