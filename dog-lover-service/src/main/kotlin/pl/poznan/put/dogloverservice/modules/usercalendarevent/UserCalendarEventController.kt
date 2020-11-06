package pl.poznan.put.dogloverservice.modules.usercalendarevent

import java.util.UUID
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.usercalendarevent.dto.UserCalendarEventDTO
import javax.validation.Valid

@RestController
@RequestMapping("/userCalendarEvents")
class UserCalendarEventController(
        private val userCalendarEventService: UserCalendarEventService
) {

    @GetMapping
    fun getUserCalendar(): List<UserCalendarEventDTO> {
        return userCalendarEventService.getUserCalendar(getCurrentUserId())
    }

    @GetMapping("/{id}")
    fun getUserCalendarEvent(@PathVariable id: UUID): UserCalendarEventDTO {
        return userCalendarEventService.getCalendarEvent(id, getCurrentUserId())
    }

    @PostMapping
    fun saveCalendarEvent(@Valid @RequestBody userCalendarEventDTO: UserCalendarEventDTO): UserCalendarEventDTO {
        return userCalendarEventService.saveCalendarEvent(userCalendarEventDTO, getCurrentUserId())
    }

    @PutMapping
    fun updateCalendarEvent(@Valid @RequestBody userCalendarEventDTO: UserCalendarEventDTO): UserCalendarEventDTO {
        return userCalendarEventService.updateCalendarEvent(userCalendarEventDTO, getCurrentUserId())
    }
}