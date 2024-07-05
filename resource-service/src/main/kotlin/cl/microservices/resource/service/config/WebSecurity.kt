package cl.microservices.resource.service.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class WebSecurity {

    @Bean
    open fun configure(http:HttpSecurity):SecurityFilterChain  {
        var jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(AuthServerRoleConverter())
        http.run {
            authorizeHttpRequests {
                it.requestMatchers(HttpMethod.GET,"/actuator/**").permitAll()
                it.requestMatchers(HttpMethod.GET, "/api/health").authenticated()
                cors { it.disable() }
            }
            oauth2ResourceServer {
                it.jwt {
                    it.jwtAuthenticationConverter(jwtAuthenticationConverter)
                }
            }

        }
        return http.build()
    }
}