package pl.poznan.put.dogloverservice.modules.dog

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
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
        return dogService.getDog(name, getCurrentUserId())
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"))
    @GetMapping
    fun getUserDogs(): List<DogDTO> {
        return dogService.getUserDogs(getCurrentUserId())
    }
}
