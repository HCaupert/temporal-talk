package fr.hcaupert.orderservice

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.orderserviceapi.*
import fr.hcaupert.paymentserviceapi.PaymentService
import fr.hcaupert.shippingserviceapi.ShippingId
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalUtils.MySearchAttributes
import fr.hcaupert.temporalutils.MyTemporalUtils.myActivity
import fr.hcaupert.temporalutils.MyTemporalUtils.mySetSearchAttribute
import io.temporal.workflow.Workflow


class ProcessOrderWorkflowImpl : ProcessOrderWorkflow {

    private val inventoryService = myActivity<InventoryService>(MyTemporalQueue.INVENTORY)
    private val paymentService = myActivity<PaymentService>(MyTemporalQueue.PAYMENT)

    private var paymentStatus = PaymentStatus.PENDING
    private var shippingStatus = ShippingStatus.PENDING
        set(value) {
            field = value
            mySetSearchAttribute(MySearchAttributes.SHIPPING_STATUS, value)
        }

    init {
        mySetSearchAttribute(MySearchAttributes.SHIPPING_STATUS, shippingStatus)
    }

    private var shippingId: ShippingId? = null

    private lateinit var order: Order

    override fun processOrder(order: Order) {
        this.order = order

        // Inventory
        inventoryService.reserveArticle(order.article.id)

        // Await Payment Authorization
        Workflow.await { paymentStatus == PaymentStatus.AUTHORIZED }

        // Prepare Shipping
        shippingStatus = ShippingStatus.AWAITING_PREPARATION
        Workflow.await { shippingStatus == ShippingStatus.PREPARED }

        // Capture Payment
        paymentService.capturePayment(order.paymentId)
        paymentStatus = PaymentStatus.CAPTURED

        // Ship item
        shippingStatus = ShippingStatus.AWAITING_SHIPPING
        Workflow.await { shippingStatus == ShippingStatus.SHIPPED }
    }

    override fun getOrder(): OrderInfo {
        return OrderInfo(
            order = order,
            shippingStatus = shippingStatus,
            trackingNumber = shippingId,
            paymentStatus = paymentStatus,
        )
    }

    override fun paymentAuthorizationReceived() {
        paymentStatus = PaymentStatus.AUTHORIZED
    }

    override fun shippingPrepared(shippingId: String) {
        this.shippingId = shippingId
        shippingStatus = ShippingStatus.PREPARED
    }

    override fun orderShipped() {
        this.shippingStatus = ShippingStatus.SHIPPED
    }
}

