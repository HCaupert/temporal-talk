package fr.hcaupert.orderserviceapi

import java.time.LocalDateTime
import java.util.*

data class Order(
    val id: UUID,
    val paymentId: UUID,
    val creationDate: LocalDateTime,
    val article: Article,
    val shipping: ShippingDetails,
)

