package cl.microservices.resource.service.common.exceptions

import reactor.core.publisher.Mono
import java.lang.RuntimeException

class WebClientException(bodyToMono: Mono<String.Companion>) : RuntimeException() {

}
