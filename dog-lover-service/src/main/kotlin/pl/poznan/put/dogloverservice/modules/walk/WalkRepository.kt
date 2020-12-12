package pl.poznan.put.dogloverservice.modules.walk

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import pl.poznan.put.dogloverservice.modules.doglover.DogLover
import java.util.*

@Repository
interface WalkRepository : JpaRepository<Walk, UUID> {

    fun findAllByDogLoverAndWalkStatusOrderByCreatedAtAsc(dogLover: DogLover,
                                                          walkStatus: WalkStatus): List<Walk>

    fun findFirstByDogLoverIdAndWalkStatusInOrderByCreatedAtDesc(dogLoverId: UUID, walkStatuses: Set<WalkStatus>): Walk?

    fun existsByDogLoverIdAndWalkStatus(dogLoverId: UUID, walkStatus: WalkStatus): Boolean

    fun findByIdAndDogLoverId(id: UUID, dogLoverId: UUID): Walk?

    fun findAllByMapMarkerIdAndWalkStatusAndDogLoverIdIsNot(mapMarkerId: UUID,
                                                            walkStatus: WalkStatus,
                                                            dogLoverId: UUID): List<Walk>

    fun findAllByMapMarkerIdInAndWalkStatusInAndDogLoverIdIn(mapMarkerIds: List<UUID>,
                                                             walkStatuses: List<WalkStatus>,
                                                             dogLoverIds: List<UUID>): List<Walk>

    @Transactional
    @Modifying
    @Query(
            value = """
                UPDATE Walk w
                SET w.walkStatus = CASE
                                       WHEN w.walkStatus = 'ONGOING' THEN 'CANCELED'
                                       ELSE 'LEFT_DESTINATION'
                    END
                WHERE w.walkStatus IN ('ONGOING', 'ARRIVED_AT_DESTINATION')
                  AND w.dogLover.id = :dogLoverId 
            """
    )
    fun completeDogLoverStartedWalks(@Param("dogLoverId") dogLoverId: UUID)
}