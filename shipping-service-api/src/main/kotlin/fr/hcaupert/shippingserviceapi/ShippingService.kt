package fr.hcaupert.shippingserviceapi

import fr.hcaupert.orderserviceapi.Order

interface ShippingService {
    fun shipOrder(order: Order)
}
