package pl.poznan.put.dogloverservice.modules.locationrecommendation

import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus.BLOCKS
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.RelationshipStatus.FOLLOWS
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkService
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO
import java.util.*
import kotlin.math.pow

@Service
class LocationRecommendationService(
        private val mapMarkerService: MapMarkerService,
        private val walkService: WalkService,
        private val dogLoverRelationshipService: DogLoverRelationshipService
) {
    fun getRecommendedLocations(
        dogLoverId: UUID,
        dogLoverLatitude: Double,
        dogLoverLongitude: Double
    ): List<LocationRecommendationDTO> {
        val neighbourhoodLocations = mapMarkerService.getNeighbourhoodMapMarkers(dogLoverLatitude, dogLoverLongitude)

        if (neighbourhoodLocations.isEmpty())
            return emptyList()

        val dogLoverRelationships = dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)
        val dogLoversInLocations = walkService.getDogLoversInLocations(
                neighbourhoodLocations.map { it.id },
                dogLoverRelationships)

        val locationRecommendationsWithInitialRatings = neighbourhoodLocations.mapIndexed { index, mapMarkerRecommendationDTO ->
            val dogLoversInLocation = dogLoversInLocations[mapMarkerRecommendationDTO.id] ?: emptyList()
            LocationRecommendationDTO(
                    mapMarkerRecommendation = mapMarkerRecommendationDTO,
                    dogLoversInLocation = dogLoversInLocation,
                    rating = countLocationRating(initialRating = index + 1, dogLoversInLocation)
            )
        }

        return locationRecommendationsWithInitialRatings.sortedByDescending { it.rating }
    }

    private fun countLocationRating(initialRating: Int, dogLoversInLocation: List<DogLoverInLocationDTO>): Double {
        val relationshipsCount = dogLoversInLocation
                .groupingBy { it.relationshipStatus }
                .eachCount()
                .withDefault { 0 }
        return initialRating +
                relationshipsCount.getValue(FOLLOWS).toDouble().pow(2) * 0.3 +
                relationshipsCount.getValue(FOLLOWS) * 0.8 -
                relationshipsCount.getValue(BLOCKS).toDouble().pow(2) * 1.3
    }
}