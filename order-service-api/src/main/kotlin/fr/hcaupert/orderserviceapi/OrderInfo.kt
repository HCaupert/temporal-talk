package fr.hcaupert.orderserviceapi

data class OrderInfo(
    val order: Order,
    val shippingStatus: ShippingStatus,
    val paymentStatus: PaymentStatus,
    val trackingNumber: String?,
)
