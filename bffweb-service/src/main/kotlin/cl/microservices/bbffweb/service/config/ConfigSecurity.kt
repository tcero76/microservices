package cl.microservices.bbffweb.service.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer.RedirectionEndpointConfig
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class ConfigSecurity(val clientRegistrationRepository: ClientRegistrationRepository) {
    val log = KotlinLogging.logger {  }
    @Bean
    fun config(http:HttpSecurity):SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .cors { it.disable() }
            .oauth2Client(Customizer.withDefaults())
            .oauth2Login {
                it.clientRegistrationRepository(clientRegistrationRepository)
                it.defaultSuccessUrl("http://localhost:8080/authorized")
//                it.redirectionEndpoint(customRedirectionEndpoint())
            }
            .logout {

                it.logoutUrl("/logout")
                .logoutSuccessUrl("http://auth-server:8000/logout") // Redirigir después del logout
                .invalidateHttpSession(true) // Invalidar la sesión HTTP
                .clearAuthentication(true) // Limpiar la autenticación
                .deleteCookies("JSESSIONID") // Eliminar cookies asociadas
            }
        return http.build()
    }
    private fun customAuthorizationRequestResolver(): OAuth2AuthorizationRequestResolver? {
        val authorizationRequestResolver = DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")
        authorizationRequestResolver.setAuthorizationRequestCustomizer {
            it.redirectUri("http://localhost:8080/oauth2/authorization/auth-server")
        }

        return authorizationRequestResolver
    }
}
