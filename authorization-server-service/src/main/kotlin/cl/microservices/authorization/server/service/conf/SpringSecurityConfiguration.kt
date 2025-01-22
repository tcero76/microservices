package cl.microservices.authorization.server.service.conf

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SpringSecurityConfiguration {
    @Value("\${external.port}")
    var externalPort:String = ""
    @Value("\${external.host}")
    var externalHost:String = ""
    val log = KotlinLogging.logger { }
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(http:HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).oidc(Customizer.withDefaults())
        return http
            .csrf { it.disable() }
            .formLogin(Customizer.withDefaults())
            .logout {
                it.logoutUrl("/logout") // Endpoint de logout
                    .logoutSuccessUrl("http://${externalHost}:${externalPort}")
            }
            .build()
    }
    @Bean
    fun configureSecurityFilterChain(http:HttpSecurity):SecurityFilterChain {
        return http
            .csrf { it.disable() }
            .formLogin(Customizer.withDefaults())
            .logout {
                it.logoutUrl("/logout") // Endpoint de logout
                    .logoutSuccessUrl("http://${externalHost}:${externalPort}")
            }
            .build()
    }
}