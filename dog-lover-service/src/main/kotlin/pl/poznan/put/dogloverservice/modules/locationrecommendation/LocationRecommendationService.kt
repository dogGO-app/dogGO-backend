package pl.poznan.put.dogloverservice.modules.locationrecommendation

import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.MapMarkerRecommendationDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkService
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO

@Service
class LocationRecommendationService(
        private val mapMarkerService: MapMarkerService,
        private val walkService: WalkService,
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {
    fun getRecommendedLocations(dogLoverId: UUID, dogLoverLatitude: Double, dogLoverLongitude: Double): List<LocationRecommendationDTO> {
        val neighbourhoodLocations = mapMarkerService.getNeighbourhoodLocations(dogLoverLatitude, dogLoverLongitude)

        if (neighbourhoodLocations.isEmpty())
            return emptyList()

        val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)
        val dogLoversInLocations = walkService.getDogLoversInLocations(
                neighbourhoodLocations.map { it.id },
                dogLoverRelationships)

        return neighbourhoodLocations.map {
            LocationRecommendationDTO(
                    it,
                    dogLoversInLocations[it.id] ?: emptyList(),
                    countLocationRating(it, dogLoversInLocations[it.id] ?: emptyList())
            )
        }
                .sortedBy { it.rating }
    }

    private fun countLocationRating(mapMarkerRecommendationDTO: MapMarkerRecommendationDTO, dogLoversInLocation: List<DogLoverInLocationDTO>): Double {
        //TODO
        return 0.0
    }
}