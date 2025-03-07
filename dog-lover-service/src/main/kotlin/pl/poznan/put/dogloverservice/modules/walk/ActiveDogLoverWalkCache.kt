package pl.poznan.put.dogloverservice.modules.walk

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.RemovalListener
import com.github.benmanes.caffeine.cache.Scheduler
import org.springframework.stereotype.Component
import pl.poznan.put.dogloverservice.infrastructure.exceptions.DogLoverIdNullException
import java.time.Duration
import java.util.*

private typealias DogLoverId = UUID
private typealias WalkId = UUID

@Component
class ActiveDogLoverWalkCache(
        private val walkRepository: WalkRepository
) {
    private val cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(35))
            .removalListener(RemovalListener<DogLoverId, WalkId> { dogLoverId, _, cause ->
                if (cause.wasEvicted()) {
                    walkRepository.completeDogLoverStartedWalks(
                            dogLoverId = dogLoverId ?: throw DogLoverIdNullException()
                    )
                }
            })
            .scheduler(Scheduler.systemScheduler())
            .build<DogLoverId, WalkId>()

    fun put(dogLoverId: DogLoverId, walkId: WalkId) {
        cache.put(dogLoverId, walkId)
    }
}