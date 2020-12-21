object Versions {
    const val kotlin = "1.4.10"
    const val kotest = "4.3.1"
    const val mockK = "1.10.2"
    const val springDoc = "1.5.0"
    const val caffeine = "2.8.8"
    const val testcontainers = "1.15.1"
}

object Libs {
    val testImplementations = listOf(
            "io.kotest:kotest-runner-junit5:${Versions.kotest}",
            "io.kotest:kotest-assertions-core:${Versions.kotest}",
            "io.kotest:kotest-property:${Versions.kotest}",
            "io.kotest:kotest-extensions-spring:${Versions.kotest}",
            "io.mockk:mockk:${Versions.mockK}",
            "org.springframework.security:spring-security-test"
    )

    val testcontainersTestImplementations = listOf(
            "org.testcontainers:postgresql:${Versions.testcontainers}",
            "org.testcontainers:testcontainers:${Versions.testcontainers}"
    )
}