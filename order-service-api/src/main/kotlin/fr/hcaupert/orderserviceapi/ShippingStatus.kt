package fr.hcaupert.orderserviceapi

enum class ShippingStatus {
    PENDING,
    AWAITING_PREPARATION,
    PREPARED,
    AWAITING_SHIPPING,
    SHIPPED,
    ;

    companion object {
        fun valueOfOrPending(s: String?, default: ShippingStatus = PENDING): ShippingStatus =
            values().find { it.name == s } ?: default
    }
}

