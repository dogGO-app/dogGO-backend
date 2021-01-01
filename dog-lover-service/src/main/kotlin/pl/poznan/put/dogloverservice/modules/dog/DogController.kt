package pl.poznan.put.dogloverservice.modules.dog

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.*
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO
import pl.poznan.put.dogloverservice.modules.dog.dto.UpdateDogDTO
import java.util.*

@RestController
@RequestMapping("/dogs")
class DogController(
        private val dogService: DogService
) {

    @ApiResponses(
            ApiResponse(description = "Dog created.", responseCode = "201"),
            ApiResponse(description = "Dog lover doesn't exist.", responseCode = "404"),
            ApiResponse(description = "This dog already exists for user.", responseCode = "409"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addDog(@RequestBody dog: UpdateDogDTO): DogDTO {
        return dogService.addDog(dog, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Dog updated.", responseCode = "200"),
            ApiResponse(description = "Dog lover or dog doesn't exist.", responseCode = "404"))
    @PutMapping
    fun updateDog(@RequestBody dog: UpdateDogDTO): DogDTO {
        return dogService.updateDog(dog, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"))
    @GetMapping
    fun getDogs(): List<DogDTO> {
        return dogService.getDogsInfo(getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog or dog avatar not found.", responseCode = "404")
    )
    @GetMapping("/{id}/avatar", produces = [IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE])
    fun getDogAvatar(
            @PathVariable(name = "id") dogId: UUID,
    ): ResponseEntity<ByteArray> {
        return dogService.getDogAvatar(dogId)
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Avatar image data is not correct image.", responseCode = "400")
    )
    @PutMapping("/{dogName}/avatar", consumes = [MULTIPART_FORM_DATA_VALUE])
    fun saveDogAvatar(
            @PathVariable dogName: String,
            @RequestPart avatar: MultipartFile
    ) {
        dogService.saveDogAvatar(dogName, avatar, dogLoverId = getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Dog successfully removed.", responseCode = "204"),
            ApiResponse(description = "The last one user dog - cannot be removed.", responseCode = "400"),
            ApiResponse(description = "Dog doesn't exist.", responseCode = "404"))
    @DeleteMapping("/{dogName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeDog(@PathVariable dogName: String) {
        return dogService.removeDog(dogName, dogLoverId = getCurrentUserId())
    }
}
