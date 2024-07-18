package cl.microservices.resource.service.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.apache.catalina.core.ApplicationContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.core.env.get
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class WebSecurity(val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties,
    val environment: Environment
) {
    val log = KotlinLogging.logger {  }
    @Bean
    open fun configure(http:HttpSecurity):SecurityFilterChain  {
        var jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(AuthServerRoleConverter())
        http.run {
            authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/**").authenticated()
                it.requestMatchers(HttpMethod.POST, "/api/**").authenticated()
                cors { it.disable() }
            }
            oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter)
                    it.decoder(jwtDecoder())
                }
            }

        }
        return http.build()
    }
    fun jwtDecoder(): JwtDecoder {
        var issuer:String = ""
        if (oAuth2ResourceServerProperties.getJwt().getIssuerUri() == null) {
            issuer = environment.getProperty("server.security.oauth2.resourceserver.jwt.issuer-uri") ?: ""
        } else {
            issuer = oAuth2ResourceServerProperties.getJwt().getIssuerUri()
        }
        log.info { "ISSUER: la url es: ${issuer}" }
        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer) as NimbusJwtDecoder
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}