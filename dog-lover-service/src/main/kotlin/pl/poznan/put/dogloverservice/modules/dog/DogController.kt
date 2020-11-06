package pl.poznan.put.dogloverservice.modules.dog

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.dog.dto.DogDTO

@RestController
@RequestMapping("/dogs")
class DogController(
        private val dogService: DogService
) {

    @PostMapping
    fun addDog(@RequestBody dog: DogDTO): DogDTO {
        return dogService.addDog(dog, getCurrentUserId())
    }

    @PutMapping
    fun updateDog(@RequestBody dog: DogDTO): DogDTO {
        return dogService.updateDog(dog, getCurrentUserId())
    }

    @GetMapping("/{name}")
    fun getDog(@PathVariable name: String): DogDTO {
        return dogService.getDog(name, getCurrentUserId())
    }

    @GetMapping
    fun getUserDogs(): List<DogDTO> {
        return dogService.getUserDogs(getCurrentUserId())
    }
}
