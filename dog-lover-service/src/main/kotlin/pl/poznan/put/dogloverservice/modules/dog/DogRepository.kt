package pl.poznan.put.dogloverservice.modules.dog

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface DogRepository : JpaRepository<Dog, UUID> {

    fun existsByIdAndDogLoverIdAndRemovedIsFalse(dogId: UUID, dogLoverId: UUID): Boolean

    fun existsByNameAndDogLoverIdAndRemovedIsFalse(name: String, dogLoverId: UUID): Boolean

    fun findByNameAndDogLoverIdAndRemovedIsFalse(name: String, dogLoverId: UUID): Dog?

    fun findByIdAndRemovedIsFalse(id: UUID): Dog?

    fun findAllByDogLoverIdAndRemovedIsFalse(dogLoverId: UUID): List<Dog>

    fun countAllByDogLoverIdAndRemovedIsFalse(dogLoverId: UUID): Int

    @Transactional
    @Modifying
    @Query(
            value = """
                UPDATE Dog d
                SET d.removed = TRUE 
                WHERE d.id = :dogId
                  AND d.dogLover.id = :dogLoverId
            """
    )
    fun setRemovedToTrue(@Param("dogId") dogId: UUID,
                         @Param("dogLoverId") dogLoverId: UUID)

    @Transactional
    @Modifying
    @Query(
            value = """
                UPDATE Dog d
                SET d.avatar.image = :avatarImage,
                    d.avatar.checksum = :avatarChecksum
                WHERE d.id = :dogId
                  AND d.dogLover.id = :dogLoverId
            """
    )
    fun saveAvatar(@Param("dogId") dogId: UUID,
                   @Param("dogLoverId") dogLoverId: UUID,
                   @Param("avatarImage") avatarImage: ByteArray,
                   @Param("avatarChecksum") avatarChecksum: String)
}