package pl.poznan.put.authservice.infrastructure.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ServiceException(
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        override val message: String
) : ResponseStatusException(httpStatus, message)

class InvalidActivationCodeException : ServiceException(
        HttpStatus.BAD_REQUEST,
        "Invalid activation code."
)

class UserEmailAlreadyVerifiedException : ServiceException(
        HttpStatus.CONFLICT,
        "User email is already verified."
)

class UserWithGivenEmailNotFoundException(email: String) : ServiceException(
        HttpStatus.NOT_FOUND,
        "User with email: $email doesn't exist."
)

class CacheValueNullException : ServiceException(
        HttpStatus.INTERNAL_SERVER_ERROR,
        "Cache value cannot be null."
)
