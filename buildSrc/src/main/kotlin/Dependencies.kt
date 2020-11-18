object Versions {
    const val kotlin = "1.4.10"
    const val kotest = "4.3.1"
    const val mockk = "1.10.2"
}

object Libs {
    val testImplementations = listOf(
            "io.kotest:kotest-runner-junit5:${Versions.kotest}",
            "io.kotest:kotest-assertions-core:${Versions.kotest}",
            "io.kotest:kotest-property:${Versions.kotest}",
            "io.mockk:mockk:${Versions.mockk}"
    )
}