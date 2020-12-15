package pl.poznan.put.mailservice.modules.mail

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.mockk
import io.mockk.verify
import org.springframework.mail.javamail.JavaMailSender
import org.thymeleaf.spring5.SpringTemplateEngine
import pl.poznan.put.mailservice.infrastructure.exceptions.InvalidEmailException
import javax.mail.internet.MimeMessage

class MailServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val javaMailSender = mockk<JavaMailSender>(relaxed = true)
    val templateEngine = SpringTemplateEngine()
    val senderEmail = "sender@test.pl"

    val mailService = MailService(
            javaMailSender,
            templateEngine,
            senderEmail
    )

    Given("correct email message") {
        val emailMessage = EmailData.correctAccountActivationEmail

        When("sending email") {
            mailService.sendEmail(emailMessage)

            Then("javaMailSender should be called") {
                verify(exactly = 1) {
                    javaMailSender.send(any<MimeMessage>())
                }
            }
        }
    }

    Given("incorrect email message") {
        val emailMessage = EmailData.incorrectAccountActivationEmail

        When("sending email") {
            val exception = shouldThrow<InvalidEmailException> {
                mailService.sendEmail(emailMessage)
            }

            Then("exception should have correct message") {
                exception shouldHaveMessage InvalidEmailException(emailMessage.receiverEmail).message
            }
        }
    }

    Given("correct and incorrect email messages") {
        val emailMessages = EmailData.emailMessages
        val correctEmailMessagesCount = EmailData.correctEmailMessages.count()

        When("sending multiple email messages") {
            shouldNotThrowAny {
                mailService.sendEmails(emailMessages)
            }

            Then("javaMailSender should be invoked for every correct message") {
                verify(exactly = correctEmailMessagesCount) {
                    javaMailSender.send(any<MimeMessage>())
                }
            }
        }
    }
})