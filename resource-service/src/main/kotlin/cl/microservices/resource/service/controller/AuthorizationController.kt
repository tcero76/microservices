package cl.microservices.resource.service.controller

import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse
import cl.microservices.resource.service.service.PaymentService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
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
    fun getHealth(@AuthenticationPrincipal jwt:Jwt):MutableMap<String,Any> {
        log.info { "JWT: El id de Usuario es: ${jwt.claims.get("user_id")}" }
        return Collections.singletonMap("principal", jwt)
    }
    @PostMapping("/api/payments")
    fun postPayments(@RequestBody commandPayment: CommandPayment, @AuthenticationPrincipal jwt: Jwt):ResponseEntity<PaymentsResponse> {
        val idUser = jwt.claims.get("id_user").toString();
        log.info { "USERID: The user_id es: ${idUser}" }
        val paymentResponse = paymentService.paymentPersist(commandPayment, idUser)
        return ResponseEntity
            .created(URI("/api/payments"))
            .body(paymentResponse.get())
    }
    @GetMapping("/api/payments")
    fun getPayments(@AuthenticationPrincipal jwt:Jwt):ResponseEntity<CreditEntryResponse> {
        log.info { "Jwt: Principal : ${jwt}" }
        val creditEntryResponse: CreditEntryResponse = paymentService.getPayments(jwt.claims.get("id_user").toString())
        log.info { "PAYMENTSERVICE: Respuesta : ${creditEntryResponse}" }
        return ResponseEntity
            .accepted()
            .body(creditEntryResponse)
    }
}