package cl.microservices.bbffweb.service.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
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
    @Value("\${external.host}")
    var externalHost:String = ""
    @Value("\${external.port}")
    var externalPort:String = ""
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
                it.defaultSuccessUrl("http://${externalHost}:${externalPort}/authorized")
//                it.redirectionEndpoint(customRedirectionEndpoint())
            }
            .logout {

                it.logoutUrl("/logout")
                .logoutSuccessUrl("http://auth-server:8000/logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
            }
        return http.build()
    }
    private fun customAuthorizationRequestResolver(): OAuth2AuthorizationRequestResolver? {
        val authorizationRequestResolver = DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization")
        authorizationRequestResolver.setAuthorizationRequestCustomizer {
            it.redirectUri("http://${externalHost}:${externalPort}/oauth2/authorization/auth-server")
        }

        return authorizationRequestResolver
    }
}
