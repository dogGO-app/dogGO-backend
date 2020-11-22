package pl.poznan.put.dogloverservice.modules.doglover

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.validation.Valid

@RestController
@RequestMapping("/profile") // TODO Discuss if this path should be changed
class DogLoverController(
        private val dogLoverService: DogLoverService
) {

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog lover doesn't exist.", responseCode = "404"))
    @GetMapping
    fun getDogLoverProfile(): DogLoverProfileDTO =
        dogLoverService.getDogLoverProfile(getCurrentUserId())

    @ApiResponses(
            ApiResponse(description = "Dog lover profile updated.", responseCode = "200"),
            ApiResponse(description = "Firstname or lastname is empty, or age is negative.", responseCode = "400"))
    @PutMapping
    fun updateDogLoverProfile(@Valid @RequestBody dogLoverProfile: DogLoverProfileDTO): DogLoverProfileDTO =
            dogLoverService.updateDogLoverProfile(dogLoverProfile, getCurrentUserId())
}