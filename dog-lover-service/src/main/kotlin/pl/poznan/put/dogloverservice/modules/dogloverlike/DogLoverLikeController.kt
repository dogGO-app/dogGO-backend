package pl.poznan.put.dogloverservice.modules.dogloverlike

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import java.util.*

@RestController
@RequestMapping("/dog-lover-likes")
class DogLoverLikeController(
        private val dogLoverLikeService: DogLoverLikeService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    // TODO Swagger response codes
    fun addLike(@RequestParam receiverDogLoverId: UUID) {
        dogLoverLikeService.addLike(giverDogLoverId = getCurrentUserId(), receiverDogLoverId)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    // TODO Swagger response codes
    fun removeLike(@RequestParam receiverDogLoverId: UUID) {
        dogLoverLikeService.removeLike(giverDogLoverId = getCurrentUserId(), receiverDogLoverId)
    }
}