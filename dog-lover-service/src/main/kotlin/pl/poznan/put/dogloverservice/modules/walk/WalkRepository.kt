package pl.poznan.put.dogloverservice.modules.walk

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRepository : JpaRepository<Walk, UUID> {

    fun findByDogLoverIdAndWalkStatus(dogLoverId: UUID, walkStatus: WalkStatus): Walk?

    fun existsByDogLoverIdAndWalkStatus(dogLoverId: UUID, walkStatus: WalkStatus): Boolean

    fun findByIdAndDogLoverId(id: UUID, dogLoverId: UUID): Walk?

    fun findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(mapMarkerId: UUID, walkStatus: WalkStatus, dogLoverId: UUID): List<Walk>
}