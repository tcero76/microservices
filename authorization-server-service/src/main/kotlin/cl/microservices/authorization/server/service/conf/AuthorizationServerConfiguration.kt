package cl.microservices.authorization.server.service.conf

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.security.KeyFactory
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.time.Instant
import java.util.*
import java.util.stream.Collectors


@Configuration
@EnableWebSecurity

class AuthorizationServerConfiguration {
    val log = KotlinLogging.logger { }
    @Value("\${server.port}")
    var port:String = ""
    @Value("\${container.hostname}")
    var hostname:String = ""
    @Value("\${external.port}")
    var externalPort:String = ""
    @Value("\${public-key-location}")
    var publicKeyLocation:String = ""
    @Value("\${private-key-location}")
    var privateKeyLocation:String = ""
    @Bean
    fun registeringClientRepository():RegisteredClientRepository {
        val registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("client1")
            .clientSecret("{noop}myClientService")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .redirectUri("http://localhost:${externalPort}/login/oauth2/code/users-client-oidc")
            .redirectUri("http://localhost:${externalPort}/authorized")
            .scope(OidcScopes.OPENID)
            .scope("read")
            .clientSettings(ClientSettings.builder()
                .requireAuthorizationConsent(false)
                .requireProofKey(true).build())
            .build()
        return InMemoryRegisteredClientRepository(registeredClient);
    }
    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer { context ->
            if (context.tokenType === OAuth2TokenType.ACCESS_TOKEN) {
                val principal = context.getPrincipal<Authentication>()
                val authorities = principal.authorities.stream()
                    .map {
                        it.authority
                    }
                    .collect(Collectors.toSet())
                context.claims.claim("roles", authorities)
                context.claims.claim("id_user", (principal.principal as cl.microservices.authorization.server.service.model.User).user_id)
                context.claims.issuer("http://${hostname}:${port}")
                context.claims.expiresAt(Instant.now().plusSeconds(600))
            }
        }
    }
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    fun authorizationServerSecurityFilterChain(http:HttpSecurity): SecurityFilterChain {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java).oidc(Customizer.withDefaults())
        return http
            .formLogin(Customizer.withDefaults())
            .build()
    }

    @Bean
    fun clientSettings() :ClientSettings {
        return ClientSettings.builder()
            .requireAuthorizationConsent(false)
            .requireProofKey(true)
            .build()
    }

    @Bean
    fun authorizationServerSettings():AuthorizationServerSettings {
        return AuthorizationServerSettings.builder()
            .issuer("http://${hostname}:${port}")
            .build()
    }

    @Bean
    fun jwkSource():JWKSource<SecurityContext> {
        val rsaKey:RSAKey = generateRsa()
        val jwkSet = JWKSet(rsaKey)
        return JWKSource { jwkSelector, securityContext -> jwkSelector.select(jwkSet) }
    }

    private fun loadPrivateKey():RSAPrivateKey {
        val privateKeyPEM = privateKeyLocation
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "")
            .replace("\n", "");
        val decoded = Base64.getDecoder().decode(privateKeyPEM);
        val spec = PKCS8EncodedKeySpec(decoded);
        val keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec) as RSAPrivateKey;
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

    private fun generateRsa(): RSAKey {
        val publicKey = loadPublicKey()
        val privateKey = loadPrivateKey()
        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }
}