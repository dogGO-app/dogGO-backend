package pl.poznan.put.dogloverservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ServiceException(
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        override val message: String
) : ResponseStatusException(httpStatus, message)

class DogLoverNotFoundException : ServiceException(HttpStatus.NOT_FOUND, "Dog lover profile not found! It may be not created yet.")