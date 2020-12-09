package pl.poznan.put.dogloverservice.modules.walk

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.util.*

@Repository
interface WalkRepository : JpaRepository<Walk, UUID> {

    fun findAllByDogLoverOrderByCreatedAtAsc(dogLover: DogLover): List<Walk>

    fun findFirstByDogLoverIdAndWalkStatusOrderByCreatedAtDesc(dogLoverId: UUID, walkStatus: WalkStatus): Walk?

    fun existsByDogLoverIdAndWalkStatus(dogLoverId: UUID, walkStatus: WalkStatus): Boolean

    fun findByIdAndDogLoverId(id: UUID, dogLoverId: UUID): Walk?

    fun findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(mapMarkerId: UUID,
                                                            walkStatus: WalkStatus,
                                                            dogLoverId: UUID): List<Walk>

    fun findAllByMapMarkerIdInAndWalkStatusInAndDogLoverIdIn(mapMarkerIds: List<UUID>,
                                                             walkStatuses: List<WalkStatus>,
                                                             dogLoverIds: List<UUID>): List<Walk>
}