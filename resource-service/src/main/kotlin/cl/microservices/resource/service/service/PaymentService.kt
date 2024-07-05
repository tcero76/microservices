package cl.microservices.resource.service.service

import cl.microservices.resource.service.dto.CommandPayment

interface PaymentService {
    fun paymentPersist(commandPayment: CommandPayment):Boolean
}