package fr.hcaupert.orderservice

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.PaymentStatus
import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import fr.hcaupert.orderserviceapi.ShippingStatus
import fr.hcaupert.paymentserviceapi.PaymentService
import fr.hcaupert.shippingserviceapi.ShippingService
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalUtils
import fr.hcaupert.temporalutils.MyTemporalUtils.mySetSearchAttribute
import io.temporal.activity.ActivityOptions
import io.temporal.activity.setRetryOptions
import io.temporal.workflow.Workflow
import org.springframework.stereotype.Service
import java.time.Duration


class ProcessOrderWorkflowImpl : ProcessOrderWorkflow {
    private val options = ActivityOptions {
        setTaskQueue(MyTemporalQueue.SHIPPING.name)
        setStartToCloseTimeout(Duration.ofSeconds(10))
        setRetryOptions {
            setMaximumAttempts(3)
        }
    }
    private val shippingService: ShippingService = Workflow.newActivityStub(ShippingService::class.java, options)
    private val inventoryService: InventoryService = MyTemporalUtils.myActivity(MyTemporalQueue.INVENTORY)
    private val paymentService: PaymentService = MyTemporalUtils.myActivity(MyTemporalQueue.PAYMENT)


    private lateinit var order: Order

    override fun processOrder(order: Order) {
        this.order = order
        updateShippingStatus(order.shipping.status)

        //Inventory
        inventoryService.reserveArticle(order.article.id)

        //Payment Authorization
        Workflow.await { order.payment.status == PaymentStatus.AUTHORIZED }
        updateShippingStatus(ShippingStatus.AWAITING_PREPARATION)

        // Prepare shipping
        Workflow.await { order.shipping.status == ShippingStatus.PREPARED }

        //Payment Capture
        paymentService.capturePayment(order.payment.id)
        order.payment.status = PaymentStatus.CAPTURED
        updateShippingStatus(ShippingStatus.AWAITING_SHIPPING)

        // Shipping
        Workflow.await { order.shipping.status == ShippingStatus.SHIPPED }
    }

    private fun updateShippingStatus(shippingStatus: ShippingStatus) {
        order.shipping.status = shippingStatus
        mySetSearchAttribute(
            MyTemporalUtils.MySearchAttributes.SHIPPING_STATUS,
            shippingStatus.toString()
        )
    }

    override fun getOrder() = order

    override fun markPaymentAsAuthorized() {
        order.payment.status = PaymentStatus.AUTHORIZED
    }

    override fun markOrderAsPrepared(trackingNumber: String) {
        order.shipping.trackingNumber = trackingNumber
        updateShippingStatus(ShippingStatus.PREPARED)
    }

    override fun orderShipped() = updateShippingStatus(ShippingStatus.SHIPPED)
}

