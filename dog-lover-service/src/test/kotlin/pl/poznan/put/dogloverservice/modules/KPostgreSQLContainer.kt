package pl.poznan.put.dogloverservice.modules

import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLContainer(
        dockerImageName: String
) : PostgreSQLContainer<KPostgreSQLContainer>(dockerImageName) {
    companion object {
        fun create(
                dockerImageName: String = "postgres:13.1-alpine"
        ) =
                KPostgreSQLContainer(dockerImageName).apply {
                    withDatabaseName("dog-lover-db")
                    withExposedPorts(5433)
                    withUsername("postgres")
                    withPassword("1")
                }
    }
}