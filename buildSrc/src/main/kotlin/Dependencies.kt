object Versions {
    const val kotlin = "1.4.10"
    const val kotest = "4.3.1"
    const val mockK = "1.10.2"
    const val springDoc = "1.5.0"
    const val caffeine = "2.8.8"
}

object Libs {
    val testImplementations = listOf(
            "io.kotest:kotest-runner-junit5:${Versions.kotest}",
            "io.kotest:kotest-assertions-core:${Versions.kotest}",
            "io.kotest:kotest-property:${Versions.kotest}",
            "io.mockk:mockk:${Versions.mockK}"
    )
}