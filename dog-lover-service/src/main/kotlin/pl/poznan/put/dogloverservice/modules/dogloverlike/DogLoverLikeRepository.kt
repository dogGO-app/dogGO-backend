package pl.poznan.put.dogloverservice.modules.dogloverlike

import org.springframework.data.jpa.repository.JpaRepository

interface DogLoverLikeRepository : JpaRepository<DogLoverLike, DogLoverLikeId>