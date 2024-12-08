package cl.microservices.authorization.server.service.conf

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher


@Configuration
class SpringSecurityConfiguration {
    @Bean
    fun configureSecurityFilterChain(http:HttpSecurity):SecurityFilterChain {
        http.run {
            authorizeHttpRequests { it.anyRequest().permitAll() } }
            .formLogin(Customizer.withDefaults())
            .logout {
                it.logoutRequestMatcher(AntPathRequestMatcher("/logout"))
                    .permitAll()
            }
        return http.build()
    }
}