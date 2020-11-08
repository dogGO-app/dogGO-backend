package pl.poznan.put.mailservice.infrastructure.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ServiceException(
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        override val message: String
) : ResponseStatusException(httpStatus, message)

class MailSendException(receiverEmail: String) : ServiceException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Sending mail to $receiverEmail failed."
)

class InvalidEmailException(email: String) : ServiceException(
        HttpStatus.BAD_REQUEST,
        "Invalid email address: $email."
)