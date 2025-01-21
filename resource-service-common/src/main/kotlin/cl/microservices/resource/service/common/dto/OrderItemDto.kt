package cl.microservices.resource.service.dto

import jakarta.validation.constraints.NotNull
import java.util.*

class OrderItemDto(
    @NotNull val productId: String = "",
    @NotNull val quantity: Int = 0,
    @NotNull val price: Int = 0,
    @NotNull val subTotal: Int = 0)