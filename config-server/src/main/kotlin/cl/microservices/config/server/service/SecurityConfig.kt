package cl.microservices.config.server.service

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig {
    @Bean
    @Throws(Exception::class)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf { it.disable() }
            .authorizeHttpRequests { it.requestMatchers("/actuator/**","/encrypt","/decrypt").permitAll() }
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .httpBasic(Customizer.withDefaults())
        return http.build()
    }
}