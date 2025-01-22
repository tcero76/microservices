package cl.microservices.bbffweb.service.service.impl

import cl.microservices.bbffweb.service.service.PaymentsService
import cl.microservices.config.data.BFFWebConfigData
import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse
import cl.microservices.resource.service.common.exceptions.PaymentWebClientException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import java.util.*
import java.util.function.Function
import java.util.function.Predicate
@Service
@EnableConfigurationProperties(BFFWebConfigData::class)
class PaymentsServiceImpl(
        val webClientBuilder: WebClient.Builder
) : PaymentsService {

    @Autowired
    public var webClientConfigData: BFFWebConfigData = BFFWebConfigData()
    override fun getPayment(): CreditEntryResponse {
        return getWebClient()
            .bodyToMono(CreditEntryResponse::class.java)
            .log()
            .block() ?: CreditEntryResponse(UUID.fromString("").toString(), 0)
    }

    private fun getWebClient(): WebClient.ResponseSpec  {
        return webClientBuilder
            .build()
            .method(HttpMethod.valueOf(webClientConfigData.queryPayment.method))
            .uri(webClientConfigData.queryPayment.uri)
            .accept(MediaType.valueOf(webClientConfigData.queryPayment.accept))
            .retrieve()
            .onStatus(
                Predicate<HttpStatusCode> { httpStatus: HttpStatusCode -> httpStatus == HttpStatus.UNAUTHORIZED },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse? ->
                    Mono.just<BadCredentialsException>(
                        BadCredentialsException("Not authenticated!")
                    )
                })
            .onStatus(
                Predicate<HttpStatusCode> { obj: HttpStatusCode -> obj.is4xxClientError },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse ->
                    Mono.just<Throwable>(
                        PaymentWebClientException(clientResponse.statusCode().toString())
                    )
                })
            .onStatus(
                Predicate<HttpStatusCode> { obj: HttpStatusCode -> obj.is5xxServerError },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse ->
                    Mono.just<Exception>(
                        Exception(clientResponse.statusCode().toString())
                    )
                })
    }

    override fun postPayment(commandPayment:CommandPayment): PaymentsResponse {
        return postWebClient(commandPayment)
                .bodyToMono(PaymentsResponse::class.java)
                .log()
                .block() ?: PaymentsResponse(UUID.fromString(""), 0)
    }
    private fun postWebClient(commandPayment:CommandPayment): WebClient.ResponseSpec {
        return webClientBuilder
            .build()
            .method(HttpMethod.valueOf(webClientConfigData.postPayment.method))
            .uri(webClientConfigData.postPayment.uri)
            .accept(MediaType.valueOf(webClientConfigData.postPayment.accept))
            .body(
                BodyInserters.fromPublisher<Any, Mono<Any>>(
                    Mono.just<Any>(commandPayment),
                    createParameterizedTypeReference<Any>()
                )
            )
            .retrieve()
            .onStatus(
                Predicate<HttpStatusCode> { httpStatus: HttpStatusCode -> httpStatus == HttpStatus.UNAUTHORIZED },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse? ->
                    Mono.just<BadCredentialsException>(
                        BadCredentialsException("Not authenticated!")
                    )
                })
            .onStatus(
                Predicate<HttpStatusCode> { obj: HttpStatusCode -> obj.is4xxClientError },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse ->
                    Mono.just<Throwable>(
                        PaymentWebClientException(clientResponse.statusCode().toString())
                    )
                })
            .onStatus(
                Predicate<HttpStatusCode> { obj: HttpStatusCode -> obj.is5xxServerError },
                Function<ClientResponse, Mono<out Throwable>> { clientResponse: ClientResponse ->
                    Mono.just<Exception>(
                        Exception(clientResponse.statusCode().toString())
                    )
                })
    }

    private fun <T> createParameterizedTypeReference(): ParameterizedTypeReference<T> {
        return object : ParameterizedTypeReference<T>() {
        }
    }
}