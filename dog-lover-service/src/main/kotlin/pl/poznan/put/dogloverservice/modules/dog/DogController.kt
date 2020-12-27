package pl.poznan.put.dogloverservice.modules.dog

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO

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
    fun addDog(@RequestBody dog: DogDTO): DogDTO {
        return dogService.addDog(dog, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Dog updated.", responseCode = "200"),
            ApiResponse(description = "Dog lover or dog doesn't exist.", responseCode = "404"))
    @PutMapping
    fun updateDog(@RequestBody dog: DogDTO): DogDTO {
        return dogService.updateDog(dog, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"),
            ApiResponse(description = "Dog doesn't exist.", responseCode = "404"))
    @GetMapping("/{name}")
    fun getDog(@PathVariable name: String): DogDTO {
        return dogService.getDogInfo(name, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"))
    @GetMapping
    fun getDogLoverDogs(): List<DogDTO> {
        return dogService.getDogLoverDogsInfo(getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Dog successfully removed.", responseCode = "204"),
            ApiResponse(description = "Dog doesn't exist.", responseCode = "404"))
    @DeleteMapping("/{dogName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeDog(@PathVariable dogName: String) {
        return dogService.removeDog(getCurrentUserId(), dogName)
    }
}
