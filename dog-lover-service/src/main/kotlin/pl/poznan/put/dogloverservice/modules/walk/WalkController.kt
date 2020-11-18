package pl.poznan.put.dogloverservice.modules.walk

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import java.util.UUID
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO

@RestController
@RequestMapping("/walks")
class WalkController(
        private val walkService: WalkService
) {

    @ApiResponses(
            ApiResponse(description = "Walk created.", responseCode = "201"),
            ApiResponse(description = "Dog lover, dog or map marker doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Dog lover is already on walk - cannot add another walk.", responseCode = "409"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveWalk(@RequestBody walkDTO: WalkDTO): WalkDTO {
        return walkService.saveWalk(walkDTO, AuthCommons.getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Walk status updated.", responseCode = "200"),
            ApiResponse(description = "New walk status is forbidden", responseCode = "403"),
            ApiResponse(description = "Dog lover or walk doesn't exist.", responseCode = "404"))
    @PutMapping("/{walkId}")
    fun updateWalkStatus(@PathVariable walkId: UUID, @RequestParam walkStatus: WalkStatus): WalkDTO {
        return walkService.updateWalkStatus(walkId, walkStatus, AuthCommons.getCurrentUserId())
    }
}