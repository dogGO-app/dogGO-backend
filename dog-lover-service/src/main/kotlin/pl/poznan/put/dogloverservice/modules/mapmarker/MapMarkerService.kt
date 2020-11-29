package pl.poznan.put.dogloverservice.modules.mapmarker

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerAlreadyExistsException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerNotFoundException
import pl.poznan.put.dogloverservice.infrastructure.exceptions.MapMarkerTooCloseException
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import java.lang.Math.toRadians
import java.time.Instant
import java.util.*
import kotlin.math.*

@Service
class MapMarkerService(
        private val mapMarkerRepository: MapMarkerRepository
) {
    private final val earthRadius = 6371000.0
    private final val maxDistance = 300.0

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

    private fun validateMapMarkerNotAlreadyExists(id: UUID) {
        if (mapMarkerRepository.existsById(id))
            throw MapMarkerAlreadyExistsException(id)
    }

    private fun validateNewMapMarkerIsFarEnough(mapMarker: MapMarker) {
        mapMarkerRepository.findAll().forEach {
            if (countDistance(mapMarker, it) < maxDistance)
                throw MapMarkerTooCloseException(mapMarker.latitude, mapMarker.longitude)
        }
    }

    private fun countDistance(newMapMarker: MapMarker, existingMapMarker: MapMarker): Double {
        val latitudeDiff = toRadians(newMapMarker.latitude - existingMapMarker.latitude)
        val longitudeDiff = toRadians(newMapMarker.longitude - existingMapMarker.longitude)
        val a = sin(latitudeDiff / 2).pow(2) + cos(toRadians(newMapMarker.latitude)) * cos(toRadians(newMapMarker.longitude)) * sin(longitudeDiff / 2).pow(2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

}