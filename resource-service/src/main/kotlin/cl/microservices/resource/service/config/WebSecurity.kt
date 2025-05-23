package cl.microservices.resource.service.config

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.*
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain
import java.security.KeyFactory
import java.security.interfaces.RSAPublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class WebSecurity(val oAuth2ResourceServerProperties: OAuth2ResourceServerProperties,
) {
    val log = KotlinLogging.logger {  }
    @Value("\${public-key-location}")
    var publicKeyLocation:String = ""
    @Bean
    open fun configure(http:HttpSecurity):SecurityFilterChain  {
        http
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                it.requestMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
            }
            .oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter())
                    it.decoder(jwtDecoder())
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
        val list = it.claims.get("roles").safeCast<ArrayList<String>>()
        list?.stream()?.map(::SimpleGrantedAuthority)
            ?.collect(Collectors.toList())
    }
    private inline fun <reified T> Any?.safeCast(): T? {
        return this as? T
    }

    private fun jwtDecoder(): JwtDecoder {
        return NimbusJwtDecoder.withPublicKey(loadPublicKey()).build()
    }
    private fun loadPublicKey(): RSAPublicKey {
        val publicKeyPEM = publicKeyLocation
            .replace("-----BEGIN PUBLIC KEY-----", "")
            .replace("-----END PUBLIC KEY-----", "")
            .replace("\n", "")
        val decoded = Base64.getDecoder().decode(publicKeyPEM)
        val spec = X509EncodedKeySpec(decoded)
        val keyFactory = KeyFactory.getInstance("RSA")
        return keyFactory.generatePublic(spec) as RSAPublicKey
    }
}