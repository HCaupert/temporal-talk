package fr.hcaupert.shippingserviceapi

import fr.hcaupert.orderserviceapi.Order
import io.temporal.activity.ActivityInterface

typealias ShippingId = String

@ActivityInterface
interface ShippingService {
    fun shipOrder(order: Order)
}
