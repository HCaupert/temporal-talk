package fr.hcaupert.gateway

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.PaymentStatus
import fr.hcaupert.orderserviceapi.ShippingStatus
import fr.hcaupert.shippingserviceapi.ShippingService
import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import java.time.Duration

class SimpleOrderWorkflowImpl : SimpleOrderWorkflow {

    private lateinit var order: Order
    private val options = ActivityOptions {
        setStartToCloseTimeout(Duration.ofSeconds(10))
    }

    private val shippingOptions = ActivityOptions {
        setStartToCloseTimeout(Duration.ofSeconds(10))
        setTaskQueue("shipping")
    }
    private val inventoryService = Workflow.newActivityStub(InventoryService::class.java, options)
    private val shippingService = Workflow.newActivityStub(ShippingService::class.java, shippingOptions)

    override fun processOrder(order: Order) {
        this.order = order
        // Inventory
        inventoryService.prepareArticle(order.article.id)
        order.shipping.status = ShippingStatus.PREPARED

        // Payment
        Workflow.await { order.payment.status == PaymentStatus.PAID }

        // Shipping
        shippingService.shipOrder(order)
        order.shipping.status = ShippingStatus.SHIPPED
    }

    override fun orderPaid(){
        order.payment.status = PaymentStatus.PAID
    }

    override fun getOrder() = order
}
