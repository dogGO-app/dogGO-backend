package pl.poznan.put.dogloverservice.modules.mapmarker

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.springframework.boot.test.context.SpringBootTest
import javax.transaction.Transactional

@Transactional
@SpringBootTest
class MapMarkerSpringBootTest(
        var mapMarkerService: MapMarkerService,
        var mapMarkerRepository: MapMarkerRepository
) : FunSpec({
    test("Should save map marker") {
        mapMarkerRepository.save(MapMarkerData.parkKonin)

        mapMarkerService.getAllMapMarkers().size shouldBe 1
    }

    test("Should be empty") {
        mapMarkerService.getAllMapMarkers()
    }
}) {
    override fun listeners() = listOf(SpringListener)
}