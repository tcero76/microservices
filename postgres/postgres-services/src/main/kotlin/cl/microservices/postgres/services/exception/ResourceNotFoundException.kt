package cl.microservices.postgres.services.exception

import java.lang.RuntimeException

class ResourceNotFoundException(override val message: String) : RuntimeException(message) {
}