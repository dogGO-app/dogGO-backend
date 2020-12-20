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

	testImplementation("junit:junit:4.12")
	testImplementation("org.springframework.boot:spring-boot-starter-test:2.4.1")
	testImplementation("io.kotest:kotest-runner-junit5:3.4.2")
	testImplementation("io.kotest:kotest-extensions-spring:4.3.2")
	testImplementation("com.h2database:h2:1.4.200")

	runtimeOnly("org.postgresql:postgresql")

	Libs.testImplementations.forEach { testImplementation(it) }
}