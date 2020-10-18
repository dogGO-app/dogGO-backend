package pl.poznan.put.dogloverservice.doglover

import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface DogLoverRepository : JpaRepository<DogLover, UUID>