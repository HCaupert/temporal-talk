package fr.hcaupert.shippingserviceapi

import fr.hcaupert.orderserviceapi.Order
import io.temporal.activity.ActivityInterface
import io.temporal.workflow.SignalMethod

@ActivityInterface
interface ShippingService {
    fun shipOrder(order: Order)
}
