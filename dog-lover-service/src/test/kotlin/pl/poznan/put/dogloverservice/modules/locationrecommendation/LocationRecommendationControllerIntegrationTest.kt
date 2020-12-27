package pl.poznan.put.dogloverservice.modules.locationrecommendation

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.locationrecommendation.dto.LocationRecommendationDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class LocationRecommendationControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should get location recommendations`() {
        //given
        val dogLoverLatitude = 52.23
        val dogLoverLongitude = 18.26

        //when
        val response = mockMvc.perform(get("/location-recommendations")
                .param("dogLoverLongitude", dogLoverLongitude.toString())
                .param("dogLoverLatitude", dogLoverLatitude.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val returnedLocationRecommendations = jacksonObjectMapper().registerModule(JavaTimeModule()).readValue<List<LocationRecommendationDTO>>(response)

        //then
        returnedLocationRecommendations.size shouldBe 1
        returnedLocationRecommendations[0].dogLoversInLocation.size shouldBe 1
    }
}