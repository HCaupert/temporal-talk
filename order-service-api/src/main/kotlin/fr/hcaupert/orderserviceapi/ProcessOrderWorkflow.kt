package fr.hcaupert.orderserviceapi

import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface ProcessOrderWorkflow {
    @WorkflowMethod
    fun processOrder(order: Order)

    @QueryMethod
    fun getOrder(): OrderInfo

    @SignalMethod
    fun paymentAuthorizationReceived()

    @SignalMethod
    fun shippingPrepared(shippingId: String)

    @SignalMethod
    fun orderShipped()
}
