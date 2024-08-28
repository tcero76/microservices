package cl.microservices.resource.service.exception.handler

import cl.microservices.postgres.services.exception.InconsistencyData
import cl.microservices.postgres.services.exception.ResourceNotFoundException
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.sql.Timestamp
import java.time.Instant

@ControllerAdvice
class PaymentsExceptions {
    val log = KotlinLogging.logger {  }
    @ExceptionHandler(value = [InconsistencyData::class])
    fun handleOrderDomainException(ex: InconsistencyData): ResponseEntity<ErrorBody> {
        log.error { "InconsistencyData>: ${ex.message}" }
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorBody(ex.message, Timestamp.from(Instant.now())))
    }
    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleOrderDomainException(ex: ResourceNotFoundException): ResponseEntity<ErrorBody> {
        log.error { "ResourceNotFoundException: ${ex.message}" }
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND).body(ErrorBody(ex.message, Timestamp.from(Instant.now())))
    }
}
