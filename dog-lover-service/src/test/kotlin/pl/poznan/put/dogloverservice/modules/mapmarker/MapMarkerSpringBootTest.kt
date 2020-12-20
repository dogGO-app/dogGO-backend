package pl.poznan.put.dogloverservice.modules.mapmarker

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.spring.SpringListener
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import javax.transaction.Transactional

@RunWith(SpringRunner::class)
@Transactional
@SpringBootTest
class MapMarkerSpringBootTest : FunSpec() {

    override fun listeners(): List<TestListener> {
        return listOf(SpringListener)
    }

    @Autowired
    private lateinit var mapMarkerService: MapMarkerService

    @Autowired
    private lateinit var mapMarkerRepository: MapMarkerRepository

    init {
        test("Should save map marker") {
            mapMarkerRepository.save(MapMarkerData.parkKonin)

            mapMarkerService.getAllMapMarkers().size shouldBe 1
        }

        test("Should be empty") {
            mapMarkerService.getAllMapMarkers()
        }
    }
}