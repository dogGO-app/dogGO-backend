package pl.poznan.put.dogloverservice.modules.mapmarker

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerTooCloseException
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDistanceDTO
import java.time.Instant
import java.util.*

@Service
class MapMarkerService(
        private val mapMarkerRepository: MapMarkerRepository
) {
    private companion object {
        const val MIN_NEIGHBOURHOOD_DISTANCE_IN_METERS = 300.0
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

    fun getNeighbourhoodMapMarkers(dogLoverLatitude: Double, dogLoverLongitude: Double): List<MapMarkerDistanceDTO> {
        return mapMarkerRepository.findNeighbourhoodMapMarkers(
                latitude = dogLoverLatitude,
                longitude = dogLoverLongitude,
                maxDistanceInMeters = MAX_NEIGHBOURHOOD_DISTANCE_IN_METERS
        )
                .map(::MapMarkerDistanceDTO)
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
                        minDistanceInMeters = MIN_NEIGHBOURHOOD_DISTANCE_IN_METERS
                )
        ) throw MapMarkerTooCloseException(mapMarker.latitude, mapMarker.longitude)
    }

}