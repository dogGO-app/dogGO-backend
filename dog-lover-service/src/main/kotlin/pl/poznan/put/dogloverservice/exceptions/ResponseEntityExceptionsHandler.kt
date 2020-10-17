package pl.poznan.put.dogloverservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ResponseEntityExceptionsHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(DogLoverNotFoundException::class)
    fun handleDogLoverNotFoundException(ex: DogLoverNotFoundException): ResponseEntity<String> {
        return ResponseEntity(ex.message, HttpStatus.NOT_FOUND)
    }
}