package pl.poznan.put.dogloverservice.modules.mapmarker

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO

class MapMarkerServiceTest: BehaviorSpec({
    isolationMode = IsolationMode.InstancePerLeaf

    val mapMarkerRepository = mockk<MapMarkerRepository>(relaxed = true)

    val mapMarkerService = MapMarkerService(
            mapMarkerRepository
    )

    Given("map marker DTO") {
        val mapMarker = MapMarkerData.parkMostowa
        val mapMarkerDTO = MapMarkerDTO(mapMarker)

        every {
           mapMarkerRepository.isMapMarkerFarEnough(
                   mapMarkerDTO.latitude,
                   mapMarkerDTO.longitude,
                   any()
           )
        } returns true
        every {
            mapMarkerRepository.save(any())
        } returns mapMarker

        When("saving map marker") {
            val returnedMapMarker = mapMarkerService.saveMapMarker(mapMarkerDTO)

            Then("returned map marker DTO should be equal to expected") {
                returnedMapMarker shouldBe mapMarkerDTO
            }
        }
    }

    Given("map marker id") {
        val mapMarker = MapMarkerData.parkMostowa
        val mapMarkerId = mapMarker.id

        every {
            mapMarkerRepository.findByIdOrNull(mapMarkerId)
        } returns mapMarker

        When("getting map marker by id") {
            val returnedMapMarker = mapMarkerService.getMapMarker(mapMarkerId)

            Then("returned map marker should be equal to expected") {
                returnedMapMarker shouldBe mapMarker
            }
        }
    }

    Given("empty parameters list") {
        val mapMarkers = MapMarkerData.parks
        val expectedMapMarkers = mapMarkers.map { MapMarkerDTO(it) }

        every {
            mapMarkerRepository.findAll()
        } returns mapMarkers

        When("getting all map markers") {
            val returnedMapMarkers = mapMarkerService.getAllMapMarkers()

            Then("returned map markers should be equal to expected") {
                returnedMapMarkers shouldBe expectedMapMarkers
            }
        }
    }

    Given("dog lover coordinates") {
        val dogLoverLatitude = 99.99
        val dogLoverLongitude = 99.99
        val parksDistanceViews = MapMarkerData.parksDistanceViews
        val expectedMapMarkers = MapMarkerData.parksWithDistances
                .sortedByDescending { it.distanceInMeters }

        every {
            mapMarkerRepository.findNeighbourhoodMapMarkers(
                    dogLoverLatitude,
                    dogLoverLongitude,
                    any()
            )
        } returns parksDistanceViews

        When("getting neighbourhood map markers") {
            val actualMapMarkers = mapMarkerService.getNeighbourhoodMapMarkers(
                    dogLoverLatitude,
                    dogLoverLongitude
            )

            Then("actual map markers should be equal to expected") {
                actualMapMarkers shouldBe expectedMapMarkers
            }
        }
    }
})