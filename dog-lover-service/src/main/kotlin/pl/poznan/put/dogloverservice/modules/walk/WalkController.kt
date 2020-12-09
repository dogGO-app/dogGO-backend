package pl.poznan.put.dogloverservice.modules.walk

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
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
    @GetMapping
    fun getWalks(): List<WalkDTO> {
        return walkService.getWalks(dogLoverId = getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Walk created.", responseCode = "201"),
            ApiResponse(description = "Dog lover, dog or map marker doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Dog lover is already on walk - cannot add another walk.", responseCode = "409"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveWalk(@RequestBody walkDTO: WalkDTO): WalkDTO {
        return walkService.saveWalk(walkDTO, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Walk status updated.", responseCode = "200"),
            ApiResponse(description = "New walk status is forbidden.", responseCode = "403"),
            ApiResponse(description = "Dog lover or walk doesn't exist.", responseCode = "404"))
    @PutMapping("/{walkId}")
    fun updateWalkStatus(@PathVariable walkId: UUID, @RequestParam walkStatus: WalkStatus): WalkDTO {
        return walkService.updateWalkStatus(walkId, walkStatus, getCurrentUserId())
    }

    @GetMapping("/dog-lovers-in-location/{mapMarkerId}")
    fun getOtherDogLoversInLocation(@PathVariable mapMarkerId: UUID): List<DogLoverInLocationDTO> {
        return walkService.getOtherDogLoversInLocation(mapMarkerId, getCurrentUserId())
    }
}