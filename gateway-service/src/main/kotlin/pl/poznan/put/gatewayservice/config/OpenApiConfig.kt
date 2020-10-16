package pl.poznan.put.gatewayservice.config

import org.springdoc.core.GroupedOpenApi
import org.springdoc.core.SwaggerUiConfigParameters
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.cloud.gateway.route.RouteDefinitionLocator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy

@Configuration
class OpenApiConfig {
    @Bean
    @RefreshScope
    @Lazy(false)
    fun apis(
            @Suppress("SpringJavaInjectionPointsAutowiringInspection")
            swaggerUiConfigParameters: SwaggerUiConfigParameters,
            locator: RouteDefinitionLocator
    ): List<GroupedOpenApi> {
        val definitions = locator.routeDefinitions.collectList().block()
        return definitions
                ?.filter { routeDefinition ->
                    routeDefinition.id.matches(".*-service".toRegex())
                }
                ?.map { routeDefinition ->
                    val name = routeDefinition.id
                    swaggerUiConfigParameters.addGroup(name)
                    GroupedOpenApi.builder()
                            .pathsToMatch("/$name/**")
                            .group(name)
                            .build()
                }
                .orEmpty()
    }
}