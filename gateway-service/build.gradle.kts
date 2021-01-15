version = "1.0.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:${Versions.springDoc}")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:${Versions.springDoc}")
}