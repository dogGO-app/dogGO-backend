package pl.poznan.put.dogloverservice.modules.mapmarker

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerTooCloseException
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.MapMarkerRecommendationDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import java.lang.Math.toRadians
import java.time.Instant
import java.util.*
import kotlin.math.*

@Service
class MapMarkerService(
        private val mapMarkerRepository: MapMarkerRepository
) {
    private companion object {
        const val EARTH_RADIUS_IN_METERS = 6371000.0
        const val MIN_DISTANCE_IN_METERS = 300.0
        const val MAX_NEIGHBOURHOOD_DISTANCE_IN_METERS = 2000.0
    }

    fun saveMapMarker(mapMarkerDTO: MapMarkerDTO): MapMarkerDTO {
        validateMapMarkerNotAlreadyExists(mapMarkerDTO.id)
        val mapMarker = MapMarker(
                mapMarkerDTO.id,
                mapMarkerDTO.name,
                mapMarkerDTO.description,
                mapMarkerDTO.latitude,
                mapMarkerDTO.longitude,
                Instant.now()
        )
        validateNewMapMarkerIsFarEnough(mapMarker)

        return MapMarkerDTO(mapMarkerRepository.save(mapMarker))
    }

    fun getAllMapMarkers(): List<MapMarkerDTO> {
        return mapMarkerRepository.findAll().map { MapMarkerDTO(it) }
    }

    fun getMapMarker(mapMarkerId: UUID): MapMarker {
        return mapMarkerRepository.findByIdOrNull(mapMarkerId)
                ?: throw MapMarkerNotFoundException(mapMarkerId)
    }

    fun getNeighbourhoodLocations(dogLoverLatitude: Double, dogLoverLongitude: Double): List<MapMarkerRecommendationDTO> {
        return mapMarkerRepository.findNeighbourhoodMapMarkers(
                dogLoverLatitude,
                dogLoverLongitude,
                MAX_NEIGHBOURHOOD_DISTANCE_IN_METERS)
                .map {
                    MapMarkerRecommendationDTO(
                            it,
                            countDistance(dogLoverLatitude, dogLoverLongitude, it.latitude, it.longitude))
                }
                .sortedByDescending { it.distanceInMeters }
    }

    private fun validateMapMarkerNotAlreadyExists(id: UUID) {
        if (mapMarkerRepository.existsById(id))
            throw MapMarkerAlreadyExistsException(id)
    }

    private fun validateNewMapMarkerIsFarEnough(mapMarker: MapMarker) {
        if (!mapMarkerRepository.isMapMarkerFarEnough(
                        latitude = mapMarker.latitude,
                        longitude = mapMarker.longitude,
                        minDistanceInMeters = MIN_DISTANCE_IN_METERS
                )
        ) throw MapMarkerTooCloseException(mapMarker.latitude, mapMarker.longitude)
    }

    private fun countDistance(
            firstLatitude: Double,
            firstLongitude: Double,
            secondLatitude: Double,
            secondLongitude: Double
    ): Double {
        val latitudeDiff = toRadians(firstLatitude - secondLatitude)
        val longitudeDiff = toRadians(firstLongitude - secondLongitude)
        val a = sin(latitudeDiff / 2).pow(2) + cos(toRadians(firstLatitude)) * cos(toRadians(firstLongitude)) * sin(longitudeDiff / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return EARTH_RADIUS_IN_METERS * c
    }

}