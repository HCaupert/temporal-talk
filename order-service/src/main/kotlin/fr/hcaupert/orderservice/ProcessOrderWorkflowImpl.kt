package fr.hcaupert.orderservice

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import fr.hcaupert.shippingserviceapi.ShippingService
import org.springframework.stereotype.Service


@Service
class ProcessOrderWorkflowImpl(
    shippingService: ShippingService,
    inventoryService: InventoryService,
) : ProcessOrderWorkflow {

    private val shippingService: ShippingService = shippingService
    private val inventoryService: InventoryService = inventoryService

    override fun processOrder(order: Order) {
        //Inventory
        inventoryService.reserveArticle(order.article.id)

        // Shipping
        shippingService.shipOrder(order)
    }
}

