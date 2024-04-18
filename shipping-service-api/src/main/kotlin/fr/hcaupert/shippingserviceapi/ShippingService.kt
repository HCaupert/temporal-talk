package fr.hcaupert.shippingserviceapi

import fr.hcaupert.orderserviceapi.Order
import io.temporal.activity.ActivityInterface

@ActivityInterface
interface ShippingService {
    fun shipOrder(order: Order)
}
