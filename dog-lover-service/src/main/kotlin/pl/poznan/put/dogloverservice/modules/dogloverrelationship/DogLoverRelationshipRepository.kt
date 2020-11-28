package pl.poznan.put.dogloverservice.modules.dogloverrelationship

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogLoverRelationshipRepository : JpaRepository<DogLoverRelationship, DogLoverRelationshipId> {

    fun findByDogLoverRelationshipId(dogLoverRelationshipId: DogLoverRelationshipId): DogLoverRelationship?

    fun findAllByDogLoverRelationshipId_GiverDogLoverId(giverDogLoverId: UUID): List<DogLoverRelationship>
}