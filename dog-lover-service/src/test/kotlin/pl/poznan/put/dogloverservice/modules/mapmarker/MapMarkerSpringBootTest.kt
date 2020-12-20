package pl.poznan.put.dogloverservice.modules.mapmarker

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import javax.transaction.Transactional

@Transactional
@SpringBootTest
@WithMockUser(authorities = ["SCOPE_dog-lover"])
@AutoConfigureMockMvc
class MapMarkerSpringBootTest(
        val mapMarkerService: MapMarkerService,
        val mapMarkerRepository: MapMarkerRepository,
        val mockMvc: MockMvc,
        val gson: Gson
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

        val listType = object : TypeToken<List<MapMarkerDTO>>() {}.type
        val foundMapMarkers = gson.fromJson<List<MapMarkerDTO>>(response, listType)

        foundMapMarkers.size shouldBe 2
        foundMapMarkers.map { it.name } shouldContainExactlyInAnyOrder listOf("Park Mostowa", "Park Konin")
    }
}) {
    override fun listeners() = listOf(SpringListener)
}