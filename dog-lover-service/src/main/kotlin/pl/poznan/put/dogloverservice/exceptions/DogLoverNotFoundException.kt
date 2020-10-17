package pl.poznan.put.dogloverservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class DogLoverNotFoundException : RuntimeException("Dog lover profile not found! It may be not created yet.")