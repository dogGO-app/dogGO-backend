package pl.poznan.put.dogloverservice.modules.locationrecommendation

import java.util.UUID
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkService

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

        val locationRecommendationsWithInitialRatings = neighbourhoodLocations.mapIndexed { index, mapMarkerRecommendationDTO ->
            LocationRecommendationDTO(
                    mapMarkerRecommendationDTO,
                    dogLoversInLocations[mapMarkerRecommendationDTO.id] ?: emptyList(),
                    (index + 1) * 4.0
            )
        }

        locationRecommendationsWithInitialRatings.forEach { it.rating = countLocationRating(it) }

        return locationRecommendationsWithInitialRatings.sortedByDescending { it.rating }
    }

    private fun countLocationRating(locationRecommendationDTO: LocationRecommendationDTO): Double {
        val relationshipsCounter = locationRecommendationDTO.dogLoversInLocation
                .groupingBy { it.relationshipStatus }
                .eachCount()
        return locationRecommendationDTO.rating +
                (relationshipsCounter[RelationshipStatus.FOLLOWS]?.times(3.0)
                        ?: 0.0) * locationRecommendationDTO.rating * 0.2 -
                (relationshipsCounter[RelationshipStatus.BLOCKS]?.times(3.0)
                        ?: 0.0) * locationRecommendationDTO.rating * 0.2
    }
}