package cl.microservices.resource.service.controller

import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.service.PaymentService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*

@RestController
class AuthorizationController(@Autowired val paymentService: PaymentService) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/api/health")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getHealth(@AuthenticationPrincipal jwt:Jwt):MutableMap<String,Any> {
        log.info { "HEALTH: entregó rol USER" }
        return Collections.singletonMap("principal", jwt)
    }

    @PostMapping("/api/payments")
    fun postPayments(@RequestBody commandPayment: CommandPayment):ResponseEntity<Boolean> {
        val paymentResponse = paymentService.paymentPersist(commandPayment)
        log.info { "PAYMENTSERVICE: Respuesta : ${paymentResponse}" }
        if (paymentResponse) {
            return ResponseEntity.created(URI("/")).body(paymentResponse)
        }
        return ResponseEntity.internalServerError().body(paymentResponse)
    }
}