package fr.hcaupert.orderserviceapi

data class ShippingDetails(
    val method: ShippingMethod,
    val address: Address,
    val receiver: Receiver,
    var status: ShippingStatus = ShippingStatus.PENDING,
    var trackingNumber: String? = null,
) {
    enum class ShippingMethod {
        EXPRESS,
        LETTER,
        STANDARD,
        ;

        companion object {
            fun random() = values().random()
        }
    }
}
