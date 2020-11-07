package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.validation.Valid

@RestController
@RequestMapping("/profile")
class DogLoverController(
        private val dogLoverService: DogLoverService
) {
    @GetMapping
    fun getDogLoverProfile(): DogLoverProfileDTO =
        dogLoverService.getDogLoverProfile(getCurrentUserId())

    @PutMapping
    fun updateDogLoverProfile(@Valid @RequestBody dogLoverProfile: DogLoverProfileDTO): DogLoverProfileDTO =
            dogLoverService.updateDogLoverProfile(dogLoverProfile, getCurrentUserId())
}