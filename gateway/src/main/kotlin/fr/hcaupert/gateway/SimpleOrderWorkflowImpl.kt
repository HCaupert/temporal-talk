package fr.hcaupert.gateway

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.ShippingStatus
import fr.hcaupert.shippingserviceapi.ShippingService

class SimpleOrderWorkflowImpl : SimpleOrderWorkflow {
    private val inventoryService: InventoryService = TODO()
    private val shippingService: ShippingService = TODO()

    override fun processOrder(order: Order) {
        // Inventory
        inventoryService.prepareArticle(order.article.id)
        order.shipping.status = ShippingStatus.PREPARED

        // Payment ??

        // Shipping
        shippingService.shipOrder(order)
        order.shipping.status = ShippingStatus.SHIPPED
    }
}
