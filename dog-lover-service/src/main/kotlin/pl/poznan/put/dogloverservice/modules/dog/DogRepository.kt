package pl.poznan.put.dogloverservice.modules.dog

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogRepository : JpaRepository<Dog, UUID> {

    fun existsByNameAndDogLoverIdAndRemovedIsFalse(name: String, dogLoverId: UUID): Boolean

    fun findByNameAndDogLoverIdAndRemovedIsFalse(name: String, dogLoverId: UUID): Dog?

    fun findAllByDogLoverIdAndRemovedIsFalse(dogLoverId: UUID): List<Dog>
}