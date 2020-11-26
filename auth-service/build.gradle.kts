version = "0.0.1-SNAPSHOT"

repositories {
	maven { url = uri("https://jitpack.io") }
}

dependencyManagement {
	imports {
		mavenBom("com.github.thomasdarimont.embedded-spring-boot-keycloak-server:embedded-keycloak-server-spring-boot-parent:2.4.0")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.security:spring-security-oauth2-resource-server")
	implementation("org.springframework.security:spring-security-oauth2-jose")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("org.springdoc:springdoc-openapi-webmvc-core:${Versions.springDoc}")

	runtimeOnly("org.postgresql:postgresql")

	implementation("org.keycloak:keycloak-admin-client:11.0.2")
	implementation("javax.ws.rs:javax.ws.rs-api:2.1.1")
	implementation("com.github.thomasdarimont.embedded-spring-boot-keycloak-server:embedded-keycloak-server-spring-boot-starter:2.4.0")

	implementation("com.github.ben-manes.caffeine:caffeine:2.8.6")

	Libs.testImplementations.forEach { testImplementation(it) }
}