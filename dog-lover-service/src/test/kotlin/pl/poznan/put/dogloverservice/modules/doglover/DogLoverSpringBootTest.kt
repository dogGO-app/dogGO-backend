package pl.poznan.put.dogloverservice.modules.doglover

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.doglover.dto.DogLoverProfileDTO
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "11443bdb-a4c9-4921-8a14-239d10189053")
@AutoConfigureMockMvc
class DogLoverSpringBootTest(
        val dogLoverRepository: DogLoverRepository,
        val mockMvc: MockMvc
) : FunSpec({

    test("Should get dog lover profile") {
        val dogLover = DogLoverData.john
        dogLoverRepository.save(dogLover)

        val response = mockMvc.perform(get("/profiles")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val dogLoverProfile = jacksonObjectMapper().readValue<DogLoverProfileDTO>(response)

        dogLoverProfile.id shouldBe dogLover.id
    }
}) {
    override fun listeners() = listOf(SpringListener)
}