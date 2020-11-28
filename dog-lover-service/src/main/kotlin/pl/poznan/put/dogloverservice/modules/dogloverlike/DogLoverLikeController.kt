package pl.poznan.put.dogloverservice.modules.dogloverlike

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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
    @ApiResponses(
            ApiResponse(description = "Dog lover successfully liked.", responseCode = "201"),
            ApiResponse(description = "Dog lovers are not currently at the same location.", responseCode = "400"),
            ApiResponse(description = "Walk with ARRIVED_AT_DESTINATION status not found.", responseCode = "404"),
            ApiResponse(description = "Dog lover has already been liked in current walk.", responseCode = "409")
    )
    fun addLike(@RequestParam receiverDogLoverId: UUID) {
        return dogLoverLikeService.addLike(giverDogLoverId = getCurrentUserId(), receiverDogLoverId)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiResponses(
            ApiResponse(description = "Like successfully removed.", responseCode = "204"),
            ApiResponse(description = "Dog lovers are not currently at the same location.", responseCode = "400"),
            ApiResponse(
                    description = "Walk with ARRIVED_AT_DESTINATION status not found or dog lover like doesn't exist.",
                    responseCode = "404"
            )
    )
    fun removeLike(@RequestParam receiverDogLoverId: UUID) {
        return dogLoverLikeService.removeLike(giverDogLoverId = getCurrentUserId(), receiverDogLoverId)
    }
}