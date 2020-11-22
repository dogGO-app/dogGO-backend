package pl.poznan.put.dogloverservice.modules.mapmarker

import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import javax.validation.Valid

@RestController
@RequestMapping("/map-markers")
class MapMarkerController(
        private val mapMarkerService: MapMarkerService
) {

    @ApiResponses(
            ApiResponse(description = "Map marker created.", responseCode = "201"),
            ApiResponse(description = "Longitude or latitude is out of range.", responseCode = "400"),
            ApiResponse(description = "Map marker already exists or is too close to another marker", responseCode = "409"))
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun saveMapMarker(@Valid @RequestBody mapMarkerDTO: MapMarkerDTO): MapMarkerDTO {
        return mapMarkerService.saveMapMarker(mapMarkerDTO)
    }

    @ApiResponses(
            ApiResponse(description = "Ok.", responseCode = "200"))
    @GetMapping
    fun getAllMapMarkers(): List<MapMarkerDTO> {
        return mapMarkerService.getAllMapMarkers()
    }
}