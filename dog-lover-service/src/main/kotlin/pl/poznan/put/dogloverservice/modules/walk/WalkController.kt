package pl.poznan.put.dogloverservice.modules.walk

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.walk.dto.CreateWalkDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import pl.poznan.put.dogloverservice.modules.walk.dto.WalkDTO
import java.util.*

@RestController
@RequestMapping("/walks")
class WalkController(
        private val walkService: WalkService
) {
    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog lover doesn't exist.", responseCode = "404"))
    @GetMapping("/history")
    fun getCompletedWalksHistory(): List<WalkDTO> {
        return walkService.getCompletedWalksHistory(dogLoverId = getCurrentUserId())
    }

    @GetMapping("/dog-lovers-in-location/{mapMarkerId}")
    fun getOtherDogLoversInLocation(@PathVariable mapMarkerId: UUID): List<DogLoverInLocationDTO> {
        return walkService.getOtherDogLoversInLocation(mapMarkerId, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Walk created.", responseCode = "201"),
            ApiResponse(description = "Dog lover, dog or map marker doesn't exist.", responseCode = "404"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveWalk(@RequestBody createWalkDTO: CreateWalkDTO): WalkDTO {
        return walkService.saveWalk(createWalkDTO, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Walk status updated.", responseCode = "200"),
            ApiResponse(description = "New walk status is forbidden.", responseCode = "403"),
            ApiResponse(description = "Dog lover or walk doesn't exist.", responseCode = "404"))
    @PutMapping("/{walkId}")
    fun updateWalkStatus(@PathVariable walkId: UUID, @RequestParam walkStatus: WalkStatus): WalkDTO {
        return walkService.updateWalkStatus(walkId, walkStatus, getCurrentUserId())
    }

    @Operation(
            summary = "Keep walk in active status",
            description = "This request has to be sent every 30 minutes during walk to keep it " +
                    "in ONGOING or ARRIVED_AT_DESTINATION status. If the request is not sent in a 30 minutes " +
                    "timespan, the walk will change its status to either CANCELED or LEFT_DESTINATION automatically.",
            responses = [
                ApiResponse(description = "Ok.", responseCode = "200"),
                ApiResponse(description = "Active dog lover walk not found.", responseCode = "404")
            ]
    )
    @PutMapping("/active")
    fun keepWalkActive() {
        walkService.keepWalkActive(dogLoverId = getCurrentUserId())
    }
}