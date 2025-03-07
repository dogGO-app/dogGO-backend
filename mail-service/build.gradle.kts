version = "1.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.boot:spring-boot-starter-mail")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:${Versions.springDoc}")
	implementation("io.github.microutils:kotlin-logging-jvm:2.0.3")
	implementation("commons-validator:commons-validator:1.7")

	Libs.testImplementations.forEach { testImplementation(it) }
}