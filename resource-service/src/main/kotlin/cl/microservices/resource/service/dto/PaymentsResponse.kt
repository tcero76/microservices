package cl.microservices.resource.service.dto

import java.util.UUID

data class PaymentsResponse(
    val customerId:UUID,
    val credit:Int
)
