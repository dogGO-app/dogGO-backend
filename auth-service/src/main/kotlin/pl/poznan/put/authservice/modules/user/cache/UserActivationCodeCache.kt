package pl.poznan.put.authservice.modules.user.cache

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.stereotype.Component
import pl.poznan.put.authservice.infrastructure.exceptions.CacheValueNullException
import java.time.Duration

typealias Email = String
typealias ActivationCode = String

@Component
class UserActivationCodeCache {
    companion object {
        const val CODE_LENGTH = 6
        val allowedChars = '0'..'9'
        val expirationDuration: Duration = Duration.ofDays(1L)
    }

    private val cache = Caffeine.newBuilder()
            .expireAfterWrite(expirationDuration)
            .build<Email, ActivationCode> {
                buildString(capacity = CODE_LENGTH) {
                    repeat(CODE_LENGTH) { append(allowedChars.random()) }
                }
            }

    fun get(key: Email): ActivationCode =
            cache.get(key) ?: throw CacheValueNullException()

    fun containsEntry(key: Email, value: ActivationCode): Boolean =
            cache.getIfPresent(key) == value
}