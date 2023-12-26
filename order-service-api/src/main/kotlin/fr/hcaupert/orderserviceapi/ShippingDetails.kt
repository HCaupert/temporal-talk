package fr.hcaupert.orderserviceapi

data class ShippingDetails(
    val shippingMethod: ShippingMethod,
    val address: Address,
    val receiver: Receiver,
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
