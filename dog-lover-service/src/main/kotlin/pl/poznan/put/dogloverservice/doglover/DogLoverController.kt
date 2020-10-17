package pl.poznan.put.dogloverservice.doglover

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.common.getUserId
import pl.poznan.put.dogloverservice.doglover.dto.DogLoverProfileDTO
import javax.validation.Valid

@RestController
@RequestMapping("/profile")
class DogLoverController(
        private val dogLoverService: DogLoverService
) {
    @GetMapping
    fun getDogLoverProfile(): DogLoverProfileDTO =
        dogLoverService.getDogLoverProfile(getUserId())

    @PutMapping
    fun updateDogLoverProfile(@Valid @RequestBody dogLoverProfile: DogLoverProfileDTO): DogLoverProfileDTO =
            dogLoverService.updateDogLoverProfile(dogLoverProfile, getUserId())
}