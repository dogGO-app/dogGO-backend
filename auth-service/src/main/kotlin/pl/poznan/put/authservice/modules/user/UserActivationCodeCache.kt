package pl.poznan.put.authservice.modules.user

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class UserActivationCodeCache {
    companion object {
        const val CODE_LENGTH = 6
        val allowedChars = '0'..'9'
        val expirationDuration: Duration = Duration.ofDays(1L)
    }

    private val cache = Caffeine.newBuilder()
            .expireAfterWrite(expirationDuration)
            .build<UUID, String> {
                buildString(capacity = CODE_LENGTH) {
                    repeat(CODE_LENGTH) { append(allowedChars.random()) }
                }
            }

    fun get(key: UUID): String =
            cache.get(key) ?: throw IllegalStateException("Cache value cannot be null!")

    fun exists(key: UUID): Boolean =
            cache.getIfPresent(key) != null
}