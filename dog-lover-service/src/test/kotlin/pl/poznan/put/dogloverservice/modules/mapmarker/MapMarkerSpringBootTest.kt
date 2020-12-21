package pl.poznan.put.dogloverservice.modules.mapmarker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
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
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "11443bdb-a4c9-4921-8a14-239d10189053")
@AutoConfigureMockMvc
class MapMarkerSpringBootTest(
        val mapMarkerService: MapMarkerService,
        val mapMarkerRepository: MapMarkerRepository,
        val mockMvc: MockMvc
) : FunSpec({
    test("Should save map marker") {
        mapMarkerRepository.save(MapMarkerData.parkKonin)

        mapMarkerService.getAllMapMarkers().size shouldBe 1
    }

    test("Should be empty") {
        mapMarkerService.getAllMapMarkers()
    }

    test("Should get all map markers") {
        MapMarkerData.parks.forEach { mapMarkerRepository.save(it) }

        val response = mockMvc.perform(get("/map-markers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString


        val foundMapMarkers = jacksonObjectMapper().readValue<List<MapMarkerDTO>>(response)

        foundMapMarkers.size shouldBe 2
        foundMapMarkers.map { it.name } shouldContainExactlyInAnyOrder listOf("Park Mostowa", "Park Konin")
    }
}) {
    override fun listeners() = listOf(SpringListener)
}