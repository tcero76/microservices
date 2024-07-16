package cl.microservices.resource.service.exception

import java.lang.RuntimeException

class ResourceNotFoundException(message: String) : RuntimeException(message) {
}