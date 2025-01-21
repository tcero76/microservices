package cl.microservices.resource.service.dto

import cl.microservices.postgres.model.Customer

data class CreditEntryResponse(
    val customerId:String,
    val total:Int
)
