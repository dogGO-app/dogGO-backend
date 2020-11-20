package pl.poznan.put.dogloverservice.infrastructure.exceptions

import java.time.Instant
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

open class ServiceException(
        httpStatus: HttpStatus = HttpStatus.INTERNAL_SERVER_ERROR,
        override val message: String
) : ResponseStatusException(httpStatus, message)

class DogLoverNotFoundException : ServiceException(
        HttpStatus.NOT_FOUND,
        "Dog lover profile not found! It may be not created yet."
)

class DogNotFoundException(name: String, userId: UUID) : ServiceException(
        HttpStatus.NOT_FOUND,
        "Dog with name: $name not exists for user with id: $userId."
)

class DogAlreadyExistsException(name: String, userId: UUID) : ServiceException(
        HttpStatus.CONFLICT,
        "Dog with name: $name already exists for user with id: $userId."
)

class MapMarkerAlreadyExistsException(id: UUID) : ServiceException(
        HttpStatus.CONFLICT,
        "Map marker with id: $id already exists!"
)

class MapMarkerTooCloseException(latitude: Double, longitude: Double) : ServiceException(
        HttpStatus.CONFLICT,
        "Map marker with latitude: $latitude and longitude: $longitude is too close!"
)

class MapMarkerNotFoundException(id: UUID) : ServiceException(
        HttpStatus.NOT_FOUND,
        "Map marker with id: $id not exists!"
)

class UserCalendarEventAlreadyExistsException(dateTime: Instant, userId: UUID, dogName: String) : ServiceException(
        HttpStatus.CONFLICT,
        "Calendar event with date and time $dateTime, userId $userId and dog name $dogName already exists."
)

class UserCalendarEventDateTimeException : ServiceException(
        HttpStatus.BAD_REQUEST,
        "Cannot save calendar event with date and time in the past"
)

class UserCalendarEventNotFoundException(calendarEventId: UUID, userId: UUID) : ServiceException(
        HttpStatus.NOT_FOUND,
        "Calendar event with id: $calendarEventId not exists for user with id: $userId."
)

class UserCalendarIdEmptyException : ServiceException(
        HttpStatus.BAD_REQUEST,
        "Calendar event id is empty."
)

class DogLoverAlreadyOnWalkException : ServiceException(
        HttpStatus.CONFLICT,
        "Dog lover is already on walk."
)

class WalkNotFoundException : ServiceException(
        HttpStatus.NOT_FOUND,
        "Walk not exists for user."
)

class WalkUpdateException : ServiceException(
        HttpStatus.FORBIDDEN,
        "Cannot update walk status - new status is forbidden."
)