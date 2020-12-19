package pl.poznan.put.dogloverservice.modules.locationrecommendation

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import pl.poznan.put.dogloverservice.modules.doglover.DogLoverData
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipData
import pl.poznan.put.dogloverservice.modules.dogloverrelationship.DogLoverRelationshipService
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerData
import pl.poznan.put.dogloverservice.modules.mapmarker.MapMarkerService
import pl.poznan.put.dogloverservice.modules.walk.WalkData
import pl.poznan.put.dogloverservice.modules.walk.WalkService
import pl.poznan.put.dogloverservice.modules.walk.dto.DogLoverInLocationDTO

class LocationRecommendationServiceTest : BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val mapMarkerService = mockk<MapMarkerService>(relaxed = true)
    val walkService = mockk<WalkService>(relaxed = true)
    val dogLoverRelationshipService = mockk<DogLoverRelationshipService>(relaxed = true)

    val locationRecommendationService = LocationRecommendationService(
            mapMarkerService,
            walkService,
            dogLoverRelationshipService
    )

    Given("dog lover id and dog lover coordinates") {
        val dogLover = DogLoverData.john
        val dogLoverId = dogLover.id
        val dogLoverLatitude = 99.99
        val dogLoverLongitude = 99.99

        And("not empty neighbourhood locations") {
            val neighbourhoodLocations = MapMarkerData.parksWithDistances
            val dogLoverRelationships = DogLoverRelationshipData.johnRelationships
            val andrew = DogLoverData.andrew
            val dogLoverInLocation = DogLoverInLocationDTO(WalkData.getArrivedAtDestination(andrew), dogLoverRelationships)
            val dogLoversInLocations = mapOf(neighbourhoodLocations.first().id
                    to listOf(dogLoverInLocation))

            val expectedLocationRecommendation = listOf(
                    LocationRecommendationDTO(
                            neighbourhoodLocations.last(),
                            emptyList(),
                            8.0
                    ),
                    LocationRecommendationDTO(
                            neighbourhoodLocations.first(),
                            listOf(dogLoverInLocation),
                            6.4
                    )
            )

            every {
                mapMarkerService.getNeighbourhoodMapMarkers(dogLoverLatitude, dogLoverLongitude)
            } returns neighbourhoodLocations
            every {
                dogLoverRelationshipService.getDogLoverRelationships(dogLoverId)
            } returns dogLoverRelationships
            every {
                walkService.getDogLoversInLocations(neighbourhoodLocations.map { it.id },
                        dogLoverRelationships)
            } returns dogLoversInLocations

            When("getting dog lover location recommendations") {
                val locationRecommendations = locationRecommendationService.getRecommendedLocations(
                        dogLoverId,
                        dogLoverLatitude,
                        dogLoverLongitude
                )

                Then("returned recommendations should be equal to expected") {
                    locationRecommendations shouldBe expectedLocationRecommendation
                }
            }
        }

        And("empty neighbourhood locations") {
            every {
                mapMarkerService.getNeighbourhoodMapMarkers(dogLoverLatitude, dogLoverLongitude)
            } returns emptyList()

            When("getting dog lover location recommendations") {
                val locationRecommendations = locationRecommendationService.getRecommendedLocations(
                        dogLoverId,
                        dogLoverLatitude,
                        dogLoverLongitude
                )

                Then("returned recommendations should be empty") {
                    locationRecommendations shouldBe emptyList()
                }
            }
        }
    }
})