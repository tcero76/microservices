package cl.microservices.resource.service.exception

import java.lang.RuntimeException

class InconsistencyData(message:String):RuntimeException(message) {
}