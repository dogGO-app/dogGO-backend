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
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	runtimeOnly("org.postgresql:postgresql")

	implementation("org.keycloak:keycloak-admin-client:11.0.2")
	implementation("com.github.thomasdarimont.embedded-spring-boot-keycloak-server:embedded-keycloak-server-spring-boot-starter:2.4.0")
}