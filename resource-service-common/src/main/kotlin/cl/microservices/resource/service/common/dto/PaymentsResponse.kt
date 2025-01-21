package cl.microservices.resource.service.common.dto

import java.util.UUID

data class PaymentsResponse(
    val customerId:UUID,
    val credit:Int
)
