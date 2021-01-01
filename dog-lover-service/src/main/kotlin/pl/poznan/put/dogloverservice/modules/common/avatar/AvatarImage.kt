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
        @Column(name = "avatar_image")
        @Basic(fetch = FetchType.LAZY)
        val image: ByteArray
) {
    init {
        validateImage()
    }

    @Column(name = "avatar_checksum")
    val checksum: String = DigestUtils.sha512_256Hex(image)

    fun getProperties(entityId: UUID): AvatarImageProperties {
        val contentType = ByteArrayInputStream(image).use {
            URLConnection.guessContentTypeFromStream(it)
        }
        val filename = run {
            val extension = contentType.substringAfter('/')
            "${entityId}_$checksum.$extension"
        }
        return AvatarImageProperties(contentType, filename)
    }

    private fun validateImage() {
        if (!image.isImage())
            throw InvalidAvatarImageException()
    }
}

data class AvatarImageProperties(val contentType: String, val filename: String)

private fun ByteArray.isImage(): Boolean =
        ByteArrayInputStream(this).use {
            ImageIO.read(it) != null
        }