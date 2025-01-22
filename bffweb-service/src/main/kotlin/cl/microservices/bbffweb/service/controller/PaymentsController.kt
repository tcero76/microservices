package cl.microservices.bbffweb.service.controller

import cl.microservices.bbffweb.service.service.PaymentsService
import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.http.HttpSession
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class PaymentsController(val paymentsService: PaymentsService) {
    val log = KotlinLogging.logger {  }
    @GetMapping("/isAuthenticated")
    fun isAuthenticated(session:HttpSession):ResponseEntity<Boolean> {
        val authentication = SecurityContextHolder.getContext().authentication
        return ResponseEntity.ok(authentication != null && authentication.isAuthenticated)
    }
    @PostMapping("/api/payments")
    fun postPayment(@RequestBody @Valid commandPayment: CommandPayment):ResponseEntity<PaymentsResponse> {
        log.info { "POST: Payment of price ${commandPayment.price}, from ${commandPayment.items.size} items" }
        val paymentsResponse =  paymentsService.postPayment(commandPayment)
         return ResponseEntity.ok(paymentsResponse)
    }
    @GetMapping("/api/payments")
    fun getPayments():ResponseEntity<CreditEntryResponse> {
        val paymentsResponse =  paymentsService.getPayment()
        return ResponseEntity
            .ok()
            .body(paymentsResponse)
    }
}