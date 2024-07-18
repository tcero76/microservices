package cl.microservices.resource.service.service

import cl.microservices.postgres.services.services.PostgresPaymentService
import cl.microservices.resource.service.adapter.PaymentToResponse
import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.CreditEntryResponse
import cl.microservices.resource.service.dto.PaymentsResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(val postgresPaymentService:PostgresPaymentService) {
    fun paymentPersist(commandPayment: CommandPayment):PaymentsResponse {
        val paymentPersist =
            postgresPaymentService.paymentPersist(commandPayment.price, commandPayment.customerId, commandPayment.items.sumOf { it.subTotal })
        return PaymentToResponse().savePaymentPostgresToPaymentResponse(paymentPersist.get())
    }

    fun getPayments(userId: String): CreditEntryResponse {
        return PaymentToResponse()
            .getPaymentPostgresToCreditEntryResponse(postgresPaymentService.getPayments(UUID.fromString(userId)))
    }
}