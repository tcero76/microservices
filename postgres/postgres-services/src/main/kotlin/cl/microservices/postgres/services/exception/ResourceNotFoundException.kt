package cl.microservices.postgres.services.exception

import java.lang.RuntimeException

class ResourceNotFoundException(message: String) : RuntimeException(message) {
}