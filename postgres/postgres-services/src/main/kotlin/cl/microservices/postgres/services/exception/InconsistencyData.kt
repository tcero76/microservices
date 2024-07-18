package cl.microservices.postgres.services.exception

import java.lang.RuntimeException

class InconsistencyData(message:String):RuntimeException(message) {
}