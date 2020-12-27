package pl.poznan.put.dogloverservice.modules.mapmarker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.AnnotationSpec
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import pl.poznan.put.dogloverservice.modules.mapmarker.dto.MapMarkerDTO
import javax.transaction.Transactional

@Transactional
@ActiveProfiles("integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WithMockUser(authorities = ["SCOPE_dog-lover"], value = "9c04f2d5-0f58-4f1b-9f1a-0a2a3a25fad5")
@AutoConfigureMockMvc
class MapMarkerControllerIntegrationTest(
        val mockMvc: MockMvc
) : AnnotationSpec() {

    override fun listeners() = listOf(SpringListener)

    @Test
    fun `Should get all map markers`() {
        //when
        val response = mockMvc.perform(get("/map-markers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk)
                .andReturn()
                .response
                .contentAsString
        val foundMapMarkers = jacksonObjectMapper().readValue<List<MapMarkerDTO>>(response)

        //then
        foundMapMarkers.size shouldBe 2
        foundMapMarkers.map { it.name } shouldContainExactlyInAnyOrder listOf("Park Mostowa", "Park Konin")
    }

    @Test
    fun `Should save map marker`() {
        //given
        val mapMarkerDTO = MapMarkerDTO(MapMarkerData.parkMostowa)

        //when
        val response = mockMvc.perform(post("/map-markers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(mapMarkerDTO)))
                .andExpect(status().isCreated)
                .andReturn()
                .response
                .contentAsString
        val returnedMapMarker = jacksonObjectMapper().readValue<MapMarkerDTO>(response)

        //then
        returnedMapMarker shouldBe mapMarkerDTO
    }

    @Test
    fun `Should throw while saving too close to existing map marker`() {
        //given
        val mapMarkerDTO = MapMarkerDTO(MapMarkerData.parkKonin)

        //when
        mockMvc.perform(post("/map-markers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jacksonObjectMapper().writeValueAsString(mapMarkerDTO)))
                //then
                .andExpect(status().isConflict)
                .andExpect(status().reason("Map marker with latitude: " +
                        "${mapMarkerDTO.latitude} and longitude: ${mapMarkerDTO.longitude} is too close!"))
    }

}