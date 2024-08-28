package cl.microservices.postgres.services.services

import cl.microservices.postgres.model.CreditEntry
import java.util.*

interface PostgresPaymentService {
    fun paymentPersist(price: Int, customerId: UUID, total: Int):Optional<CreditEntry>
    fun getPayments(customerId: UUID): CreditEntry
}