package pl.poznan.put.dogloverservice.modules.mapmarker

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface MapMarkerRepository : JpaRepository<MapMarker, UUID> {

    @Query(value = """SELECT * FROM map_marker mm
            WHERE SQRT(POW(111.12 * (mm.latitude - :lat), 2) + 
            POW(111.12 * (:lon - mm.longitude) * COS(mm.latitude / 92.215), 2)) * 1000 <= :max_dist""",
            nativeQuery = true)
    fun findNeighbourhoodMapMarkers(
            @Param("lat") latitude: Double,
            @Param("lon") longitude: Double,
            @Param("max_dist") maxDistanceInMeters: Double
    ): List<MapMarker>
}