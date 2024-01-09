package fr.hcaupert.orderserviceapi

import java.util.*

data class Payment(
    val id: UUID,
    var status: PaymentStatus = PaymentStatus.PENDING,
)
