package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class DogLoverInLocationDTO(

        val id: UUID,

        val nickname: String,

        val name: String,

        val dogs: List<DogBasicInfoDTO>,

        val likesCount: Int

) {
    constructor(dogLover: DogLover, dogs: List<DogBasicInfoDTO>) : this(
            id = dogLover.id,
            nickname = dogLover.nickname,
            name = dogLover.firstName,
            dogs = dogs,
            likesCount = dogLover.likesCount
    )
}