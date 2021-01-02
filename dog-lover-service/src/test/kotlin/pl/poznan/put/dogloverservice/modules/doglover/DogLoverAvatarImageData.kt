package pl.poznan.put.dogloverservice.modules.doglover

import org.springframework.mock.web.MockMultipartFile

object DogLoverAvatarImageData {
    val avatarBytes = javaClass.getResourceAsStream("/images/doglover.png").readBytes()

    val avatarMultipartFile
        get() = MockMultipartFile("avatar", avatarBytes)

    val invalidAvatarMultipartFile
        get() = MockMultipartFile("avatar", "not-valid-image".toByteArray())
}