version = "0.0.1-SNAPSHOT"

apply {
	plugin("org.jetbrains.kotlin.plugin.jpa")
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:${Versions.springDoc}")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")

	implementation("com.github.ben-manes.caffeine:caffeine:${Versions.caffeine}")

	runtimeOnly("org.postgresql:postgresql")

	Libs.testImplementations.forEach { testImplementation(it) }
	Libs.testcontainersTestImplementations.forEach { testImplementation(it) }
}