package pl.poznan.put.dogloverservice.modules.dog

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogRepository : JpaRepository<Dog, UUID> {

    fun existsByNameAndDogLoverId(name: String, dogLoverId: UUID): Boolean

    fun findByNameAndDogLoverId(name: String, dogLoverId: UUID): Dog?

    fun findAllByDogLoverId(dogLoverId: UUID): List<Dog>
}