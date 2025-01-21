package cl.microservices.gateway.config

import cl.microservices.config.data.GatewayConfigData
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.gateway.route.RouteLocator
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import java.util.function.Function


@Configuration
@EnableConfigurationProperties(GatewayConfigData::class)
class Router @Autowired constructor(private var gatewayConfigData: GatewayConfigData,
    val environment: Environment
) {
    val log = KotlinLogging.logger { }
    @Bean
    fun routes(builder:RouteLocatorBuilder):RouteLocator {
        val routes = builder.routes()
        routes
            .route("Authorization-server", Function { it
                .path(*gatewayConfigData.authorizationServer.path.toTypedArray())
                .uri(gatewayConfigData.authorizationServer.uri)

            })
            .route("resource-server", Function { it
                .path(*gatewayConfigData.resourceServer.path.toTypedArray())
                .uri(gatewayConfigData.resourceServer.uri)})
            .route( "frontend", Function { it
                .path(*gatewayConfigData.frontend.path.toTypedArray())
                .uri(gatewayConfigData.frontend.uri)
            })
            .route("bffweb-service", Function { it
                .path(*gatewayConfigData.bffwebService.path.toTypedArray())
                .uri(gatewayConfigData.bffwebService.uri)})
        return routes.build();
    }
}
