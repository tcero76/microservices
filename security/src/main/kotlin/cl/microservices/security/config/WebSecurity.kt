package org.example.cl.microservices.security.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator
import org.springframework.security.oauth2.core.OAuth2TokenValidator
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import java.util.stream.Collectors

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class WebSecurity(val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties,
) {
    val log = KotlinLogging.logger {  }
    @Bean
    open fun configure(http:HttpSecurity):SecurityFilterChain  {
        http.run {
            authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                it.requestMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
                cors { it.disable() }
            }
            oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    it.decoder(jwtDecoder())
                }
            }
        }
        return http.build()
    }
    fun jwtAuthenticationConverter():JwtAuthenticationConverter {
        var jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authServerRoleConverter())
        return jwtAuthenticationConverter
    }
    fun authServerRoleConverter(): Converter<Jwt, Collection<GrantedAuthority>> = Converter {
        val realmAccess = it.claims.get("roles") as Collection<String>
        (it.claims.get("roles") as ArrayList<String>).stream()
            .map(::SimpleGrantedAuthority)
            .collect(Collectors.toList())
    }

    private fun jwtDecoder(): JwtDecoder {
        var issuer:String = oAuth2ResourceServerProperties.getJwt().getIssuerUri()
        log.info { "ISSUER: la url es: ${issuer}" }
        val jwtDecoder = JwtDecoders.fromOidcIssuerLocation(issuer) as NimbusJwtDecoder
        val withIssuer = JwtValidators.createDefaultWithIssuer(issuer)
        val withAudience: OAuth2TokenValidator<Jwt> = DelegatingOAuth2TokenValidator(withIssuer)
        jwtDecoder.setJwtValidator(withAudience)
        return jwtDecoder
    }
}