package cl.microservices.postgres.services.exception

import java.lang.RuntimeException

class InconsistencyData(override val message:String):RuntimeException(message) {
}