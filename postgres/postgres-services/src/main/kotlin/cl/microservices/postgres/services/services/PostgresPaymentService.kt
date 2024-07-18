package cl.microservices.postgres.services.services

import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.Payments
import java.util.*

interface PostgresPaymentService {
    fun paymentPersist(price:Int, customer_id: UUID, total:Int):Optional<Payments>
    fun getPayments(customerId: UUID): CreditEntry
}