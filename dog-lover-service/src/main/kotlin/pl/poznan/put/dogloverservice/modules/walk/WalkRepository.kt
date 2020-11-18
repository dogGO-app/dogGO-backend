package pl.poznan.put.dogloverservice.modules.walk

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WalkRepository : JpaRepository<Walk, UUID> {

    fun existsByDogLoverIdAndWalkStatus(dogLoverId: UUID, walkStatus: WalkStatus): Boolean

    fun existsByIdAndDogLoverId(id: UUID, dogLoverId: UUID): Boolean

    fun existsByIdAndWalkStatus(id: UUID, walkStatus: WalkStatus): Boolean
}