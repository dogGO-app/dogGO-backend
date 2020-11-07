package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DogLoverRepository : JpaRepository<DogLover, UUID>