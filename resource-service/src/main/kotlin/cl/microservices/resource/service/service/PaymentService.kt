package cl.microservices.resource.service.service

import cl.microservices.postgres.services.services.PostgresPaymentService
import cl.microservices.resource.service.adapter.PaymentToResponse
import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class PaymentService(val postgresPaymentService:PostgresPaymentService) {
    fun paymentPersist(commandPayment: CommandPayment, customerId:String):Optional<PaymentsResponse> {
        val paymentPersist = postgresPaymentService.paymentPersist(commandPayment.price, UUID.fromString(customerId),  commandPayment.items.sumOf { it.subTotal })
        return Optional.of(PaymentToResponse().savePaymentPostgresToPaymentResponse(UUID.fromString(customerId), paymentPersist.get()))
    }
    fun getPayments(userId: String): CreditEntryResponse {
        return PaymentToResponse()
            .getPaymentPostgresToCreditEntryResponse(postgresPaymentService.getPayments(UUID.fromString(userId)))
    }
}