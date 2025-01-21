package cl.microservices.resource.service.common.dto

import jakarta.validation.constraints.NotNull
import java.util.*

data class CommandPayment (
    @NotNull val price:Int = 0,
    @NotNull val items: MutableList<OrderItemDto> = arrayListOf()
)
