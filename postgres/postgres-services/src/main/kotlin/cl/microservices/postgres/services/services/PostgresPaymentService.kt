package cl.microservices.postgres.services.services

import cl.microservices.postgres.model.Payments
import cl.microservices.resource.service.dto.CommandPayment
import cl.microservices.resource.service.dto.PaymentsResponse
import java.util.Optional

interface PaymentService {
    fun paymentPersist(commandPayment: CommandPayment):Optional<Payments>
    fun getPayments(s: String): PaymentsResponse
}