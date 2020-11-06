package pl.poznan.put.dogloverservice.modules.mapmarker

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import javax.validation.Valid

@RestController
@RequestMapping("/api/mapMarkers")
class MapMarkerController(
        private val mapMarkerService: MapMarkerService
) {

    @PostMapping
    fun saveMapMarker(@Valid @RequestBody mapMarkerDTO: MapMarkerDTO): MapMarkerDTO {
        return mapMarkerService.saveMapMarker(mapMarkerDTO)
    }

    @GetMapping
    fun getAllMapMarkers(): List<MapMarkerDTO> {
        return mapMarkerService.getAllMapMarkers()
    }
}