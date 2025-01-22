package cl.microservices.bbffweb.service.service

import cl.microservices.resource.service.common.dto.CommandPayment
import cl.microservices.resource.service.common.dto.CreditEntryResponse
import cl.microservices.resource.service.common.dto.PaymentsResponse

interface PaymentsService {
    fun getPayment(): CreditEntryResponse
    fun postPayment(commandPayment: CommandPayment): PaymentsResponse
}