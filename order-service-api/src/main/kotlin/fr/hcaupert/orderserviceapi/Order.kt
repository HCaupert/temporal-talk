package fr.hcaupert.orderserviceapi

import java.time.LocalDateTime
import java.util.*

data class Order(
    val id: UUID,
    val payment: Payment,
    val creationDate: LocalDateTime,
    val article: Article,
    val shipping: ShippingDetails,
)

