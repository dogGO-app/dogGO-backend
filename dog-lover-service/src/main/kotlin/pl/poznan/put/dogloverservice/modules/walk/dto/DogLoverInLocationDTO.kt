package pl.poznan.put.dogloverservice.modules.walk.dto

import java.util.UUID
import pl.poznan.put.dogloverservice.modules.dog.dto.DogBasicInfoDTO
import pl.poznan.put.dogloverservice.modules.doglover.DogLover

data class DogLoverInLocationDTO(

        val id: UUID,

        val nickname: String,

        val name: String,

        val dogs: List<DogBasicInfoDTO>

) {
    constructor(dogLover: DogLover, dogs: List<DogBasicInfoDTO>) : this(
            dogLover.id,
            dogLover.nickname,
            dogLover.firstName,
            dogs
    )
}