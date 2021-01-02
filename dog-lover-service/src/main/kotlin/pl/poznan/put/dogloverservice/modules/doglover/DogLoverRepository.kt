package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
interface DogLoverRepository : JpaRepository<DogLover, UUID> {

    fun existsByNickname(nickname: String): Boolean

    fun findByNickname(nickname: String): DogLover?

    @Transactional
    @Modifying
    @Query(
            value = """
                UPDATE DogLover dl
                SET dl.avatar.image = :avatarImage,
                    dl.avatar.checksum = :avatarChecksum
                WHERE dl.id = :dogLoverId
            """
    )
    fun saveAvatar(@Param("dogLoverId") dogLoverId: UUID,
                   @Param("avatarImage") avatarImage: ByteArray,
                   @Param("avatarChecksum") avatarChecksum: String)
}