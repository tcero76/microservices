package cl.microservices.resource.service.adapter

import cl.microservices.postgres.model.CreditEntry
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse
import java.util.UUID

class PaymentToResponse {
    fun savePaymentPostgresToPaymentResponse(customerId:UUID, payment: CreditEntry): PaymentsResponse {
        return PaymentsResponse(customerId, payment.totalcreditamount)
    }
    fun getPaymentPostgresToCreditEntryResponse(payments: CreditEntry): CreditEntryResponse {
        return CreditEntryResponse(payments.customerid.toString(), payments.totalcreditamount)
    }
}