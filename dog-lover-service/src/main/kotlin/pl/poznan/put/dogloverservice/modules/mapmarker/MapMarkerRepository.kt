package pl.poznan.put.dogloverservice.modules.mapmarker

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MapMarkerRepository : JpaRepository<MapMarker, UUID>