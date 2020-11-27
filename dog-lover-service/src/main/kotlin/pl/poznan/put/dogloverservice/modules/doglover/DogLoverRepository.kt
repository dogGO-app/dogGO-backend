package pl.poznan.put.dogloverservice.modules.doglover

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogLoverRepository : JpaRepository<DogLover, UUID> {

    fun existsByNickname(nickname: String): Boolean

    fun findByNickname(nickname: String): DogLover?
}