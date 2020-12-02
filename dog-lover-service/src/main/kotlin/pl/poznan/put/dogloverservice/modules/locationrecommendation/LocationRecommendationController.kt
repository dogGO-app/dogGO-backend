package pl.poznan.put.dogloverservice.modules.locationrecommendation

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import pl.poznan.put.dogloverservice.infrastructure.commons.AuthCommons.getCurrentUserId
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO

@RestController
@RequestMapping("/location-recommendation")
class LocationRecommendationController(
        private val locationRecommendationService: LocationRecommendationService
) {

    @GetMapping
    fun getRecommendedLocations(@RequestParam dogLoverLongitude: Double, dogLoverLatitude: Double): List<LocationRecommendationDTO> {
        return locationRecommendationService.getRecommendedLocations(getCurrentUserId(), dogLoverLatitude, dogLoverLongitude)
    }
}