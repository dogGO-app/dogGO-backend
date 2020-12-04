package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.dto.DogLoverRelationshipDTO

@RestController
@RequestMapping("/relationships")
class DogLoverRelationshipController(
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {

    @ApiResponses(
            ApiResponse(description = "Dog lover relationship created.", responseCode = "201"),
            ApiResponse(description = "Dog lover doesn't exist.", responseCode = "404"),
            ApiResponse(description = "Dog lover relationship already exists", responseCode = "409"))
    @PostMapping("/{nickname}")
    @ResponseStatus(HttpStatus.CREATED)
    fun addDogLoverRelationship(@PathVariable nickname: String, @RequestParam status: RelationshipStatus) {
        dogLoverRelationshipService.addDogLoverRelationship(getCurrentUserId(), nickname, status)
    }

    @ApiResponses(
            ApiResponse(description = "Dog lover relationship removed.", responseCode = "200"),
            ApiResponse(description = "Dog lover or relationship doesn't exist.", responseCode = "404"))
    @DeleteMapping("/{nickname}")
    fun removeDogLoverRelationship(@PathVariable nickname: String) {
        dogLoverRelationshipService.removeDogLoverRelationship(getCurrentUserId(), nickname)
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"))
    @GetMapping
    fun getDogLoverRelationships(): List<DogLoverRelationshipDTO> {
        return dogLoverRelationshipService.getDogLoverRelationshipsInfo(getCurrentUserId())
    }
}