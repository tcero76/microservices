package cl.microservices.bbffweb.service.config

import cl.microservices.config.data.BFFWebConfigData
import cl.microservices.config.data.GatewayConfigData
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit
import java.util.function.Consumer
@Configuration
@EnableConfigurationProperties(BFFWebConfigData::class)
class WebClientConfig (var bffWebConfigData: BFFWebConfigData) {

    @Value("\${security.default-client-registration-id}")
    private lateinit var defaultClientRegistrationId: String

    @Bean("webClientBuilder")
    fun webClientBuilder(clientRegistrationRepository: ClientRegistrationRepository?, oAuth2AuthorizedClientRepository: OAuth2AuthorizedClientRepository?): WebClient.Builder {
        val oauth2 = ServletOAuth2AuthorizedClientExchangeFilterFunction(clientRegistrationRepository, oAuth2AuthorizedClientRepository)
        oauth2.setDefaultOAuth2AuthorizedClient(true)
        oauth2.setDefaultClientRegistrationId(defaultClientRegistrationId)
        return WebClient.builder()
            .baseUrl(bffWebConfigData.webClient.baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, bffWebConfigData.webClient.contentType)
            .defaultHeader(HttpHeaders.ACCEPT, bffWebConfigData.webClient.acceptType)
            .clientConnector(ReactorClientHttpConnector(httpClient))
            .apply(oauth2.oauth2Configuration())
            .codecs(Consumer<ClientCodecConfigurer> { clientCodecConfigurer: ClientCodecConfigurer ->
                clientCodecConfigurer
                    .defaultCodecs()
                    .maxInMemorySize(bffWebConfigData.webClient.maxInMemorySize)
            })
    }

        private val httpClient: HttpClient
        get() = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, bffWebConfigData.webClient.connectTimeoutMs)
            .doOnConnected(Consumer<reactor.netty.Connection> { connection: reactor.netty.Connection ->
                connection.addHandlerLast(
                    ReadTimeoutHandler(
                        bffWebConfigData.webClient.readTimeoutMs,
                        TimeUnit.MILLISECONDS
                    )
                )
                connection.addHandlerLast(
                    WriteTimeoutHandler(
                        bffWebConfigData.webClient.writeTimeoutMs,
                        TimeUnit.MILLISECONDS
                    )
                )
            })
}