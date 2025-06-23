package fr.hcaupert.gateway

import fr.hcaupert.orderserviceapi.Order

interface SimpleOrderWorkflow {
    fun processOrder(order: Order)
}
