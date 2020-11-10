package pl.poznan.put.dogloverservice.modules.usercalendarevent

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.UserCalendarEventDTO
import javax.validation.Valid

@RestController
@RequestMapping("/userCalendarEvents")
class UserCalendarEventController(
        private val userCalendarEventService: UserCalendarEventService
) {

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog lover doesn't exist.", responseCode = "404"))
    @GetMapping
    fun getUserCalendar(): List<UserCalendarEventDTO> {
        return userCalendarEventService.getUserCalendar(getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog lover or calendar event doesn't exist.", responseCode = "404"))
    @GetMapping("/{id}")
    fun getUserCalendarEvent(@PathVariable id: UUID): UserCalendarEventDTO {
        return userCalendarEventService.getCalendarEvent(id, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Calendar event created.", responseCode = "201"),
            ApiResponse(description = "Description is blank or date is from the past.", responseCode = "400"),
            ApiResponse(description = "Dog lover or dog doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Calendar event already exists", responseCode = "409"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveCalendarEvent(@Valid @RequestBody userCalendarEventDTO: UserCalendarEventDTO): UserCalendarEventDTO {
        return userCalendarEventService.saveCalendarEvent(userCalendarEventDTO, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Calendar event updated.", responseCode = "200"),
            ApiResponse(description = "Description is blank or date is from the past or calendar event id is empty.", responseCode = "400"),
            ApiResponse(description = "Dog lover, dog or calendar event doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Calendar event already exists", responseCode = "409"))
    @PutMapping
    fun updateCalendarEvent(@Valid @RequestBody userCalendarEventDTO: UserCalendarEventDTO): UserCalendarEventDTO {
        return userCalendarEventService.updateCalendarEvent(userCalendarEventDTO, getCurrentUserId())
    }
}