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
                SET d.avatar.image = :avatarImage,
                    d.avatar.checksum = :avatarChecksum
                WHERE d.dogLover.id = :dogLoverId
                  AND d.name = :name
            """
    )
    fun saveAvatar(@Param("name") name: String,
                   @Param("avatarImage") avatarImage: ByteArray,
                   @Param("avatarChecksum") avatarChecksum: String,
                   @Param("dogLoverId") dogLoverId: UUID)
}