package pl.poznan.put.dogloverservice.modules.common.avatar

import org.apache.commons.codec.digest.DigestUtils
import pl.poznan.put.dogloverservice.infrastructure.exceptions.InvalidAvatarImageException
import java.io.ByteArrayInputStream
import java.net.URLConnection
import java.util.*
import javax.imageio.ImageIO
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.FetchType

@Embeddable
class AvatarImage(
        entityId: UUID,

        @Column(name = "avatar_image")
        @Basic(fetch = FetchType.LAZY)
        val image: ByteArray
) {
    init {
        if (!image.isImage()) throw InvalidAvatarImageException()
    }

    @Column(name = "avatar_checksum")
    val checksum: String = run {
        val entityIdBytes = entityId.toString().toByteArray()
        val imageChecksum = DigestUtils.sha512_256(image)
        DigestUtils.sha512_256Hex(entityIdBytes + imageChecksum)
    }

    val properties: AvatarImageProperties
        get() {
            val contentType = ByteArrayInputStream(image).use(URLConnection::guessContentTypeFromStream)
            val filename = run {
                val extension = contentType.substringAfter('/')
                "$checksum.$extension"
            }
            return AvatarImageProperties(contentType, filename)
        }
}

data class AvatarImageProperties(val contentType: String, val filename: String)

private fun ByteArray.isImage(): Boolean =
        ByteArrayInputStream(this).use {
            ImageIO.read(it) != null
        }