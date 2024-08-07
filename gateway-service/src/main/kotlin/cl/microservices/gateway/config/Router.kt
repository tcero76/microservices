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
class Router @Autowired constructor(private val environment:Environment,
    private var gatewayConfigData: GatewayConfigData
) {
    val log = KotlinLogging.logger { }
    @Bean
    fun routes(builder:RouteLocatorBuilder):RouteLocator {
        val property = environment.getProperty("spring.profiles.active").toString().split(",")
        val routes = builder.routes()
        log.info { "PROFILE: El perfil dev es: ${property.contains("dev")}" }
        log.info { "AUTHSERVER: la uri resultantes es :${gatewayConfigData.authorizationServer.uri}" }
        routes
            .route("Authorization-server", Function { it
                .path(*gatewayConfigData.authorizationServer.path.toTypedArray())
                .uri(gatewayConfigData.authorizationServer.uri)

            })
            .route("resource-server", Function { it
                .path(*gatewayConfigData.resourceServer.path.toTypedArray())
                .uri(gatewayConfigData.resourceServer.uri)})
            .build();
        if ( property.contains("dev")) {
            routes.route( "frontend-dev", Function { it
                .path(*gatewayConfigData.frontendDev.path.toTypedArray())
                .uri(gatewayConfigData.frontendDev.uri)
            })
        } else {
            routes.route("frontend-prod", Function {  it
                .path(*gatewayConfigData.frontendProd.path.toTypedArray())
                .uri(gatewayConfigData.frontendProd.uri)})
        }
        return routes.build();
    }
}
