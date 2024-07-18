package cl.microservices.resource.service.adapter

import cl.microservices.postgres.model.CreditEntry
import cl.microservices.postgres.model.Payments
import cl.microservices.resource.service.dto.CreditEntryResponse
import cl.microservices.resource.service.dto.PaymentsResponse

class PaymentToResponse {
    fun savePaymentPostgresToPaymentResponse(payment: Payments): PaymentsResponse {
        return PaymentsResponse(payment.customerid, payment.price)
    }

    fun getPaymentPostgresToCreditEntryResponse(payments: CreditEntry): CreditEntryResponse {
        return CreditEntryResponse(payments.customerid.toString(), payments.totalcreditamount)
    }
}