package pl.poznan.put.dogloverservice.modules.dog

import org.springframework.mock.web.MockMultipartFile

object DogAvatarImageData {
    val avatarBytes = javaClass.getResourceAsStream("/images/doggo.jpg").readBytes()

    val avatarMultipartFile
        get() = MockMultipartFile("avatar", avatarBytes)

    val invalidAvatarMultipartFile
        get() = MockMultipartFile("avatar", "not-valid-image".toByteArray())
}