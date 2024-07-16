package cl.sugarfever.authorization.server.service.conf

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain
import java.time.Instant
import java.util.stream.Collectors


@Configuration
@EnableWebSecurity
class SpringSecurityConfiguration {
    @Value("\${container.port}")
    var port:String? = ""
    @Value("\${container.hostname}")
    var hostname:String? = ""
    @Bean
    fun configureSecurityFilterChain(http:HttpSecurity):SecurityFilterChain {
        http.run { authorizeHttpRequests { it.anyRequest().permitAll() } }
            .formLogin(Customizer.withDefaults())
        return http.build()
    }

    fun users():UserDetailsService {
        val encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder()
        val user = User.withUsername("sergey")
            .password(encoder.encode("password"))
            .roles("USER")
            .build()
        return InMemoryUserDetailsManager(user)
    }

    @Bean
    fun jwtCustomizer():OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            if (context.tokenType === OAuth2TokenType.ACCESS_TOKEN) {
                val principal = context.getPrincipal<Authentication>()
                val authorities = principal.authorities.stream()
                    .map { it.authority }
                    .collect(Collectors.toSet())
                context.claims.claim("roles", authorities)
                context.claims.issuer("http://${hostname}:${port}")
                context.claims.expiresAt(Instant.now().plusSeconds(86400000))
            }
        }
    }
}