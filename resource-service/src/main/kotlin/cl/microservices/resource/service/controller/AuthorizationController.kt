package cl.microservices.resource.service.controller

import cl.microservices.postgres.model.Payments
import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.CreditEntryResponse
import cl.microservices.resource.service.dto.PaymentsResponse
import cl.microservices.resource.service.service.PaymentService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.*

@RestController
class AuthorizationController(val paymentService: PaymentService) {

    private val log = KotlinLogging.logger {}

    @GetMapping("/api/health")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getHealth(@AuthenticationPrincipal jwt:Jwt):MutableMap<String,Any> {
        log.info { "JWT: El id de Usuario es: ${jwt.claims.get("user_id")}" }
        return Collections.singletonMap("principal", jwt)
    }

    @GetMapping("/api/isAuthenticated")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun isAuthenticated():ResponseEntity<Boolean> {
        log.info { "SECURITY_CONTEXT: El contexto de seguridad es: ${SecurityContextHolder.getContext()}" }
        return ResponseEntity.ok(SecurityContextHolder.getContext().authentication.isAuthenticated)
    }


    @PostMapping("/api/payments")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun postPayments(@RequestBody commandPayment: CommandPayment):ResponseEntity<Payments> {
//        val paymentResponse = postgresPaymentService.paymentPersist(commandPayment)
//        log.info { "PAYMENTSERVICE: Respuesta : ${paymentResponse}" }
//        if (paymentResponse.isEmpty) {
            return ResponseEntity.created(URI("/api/payments")).body(null)
//        }
//        return ResponseEntity.created(URI("/api/payments")).body(paymentResponse.get())
    }

    @GetMapping("/api/payments")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getPayments(@AuthenticationPrincipal jwt:Jwt):ResponseEntity<CreditEntryResponse> {
        val creditEntryResponse: CreditEntryResponse = paymentService.getPayments(jwt.claims.get("id_user").toString())
        log.info { "PAYMENTSERVICE: Respuesta : ${creditEntryResponse}" }
        return ResponseEntity.accepted().body(creditEntryResponse)
    }
}