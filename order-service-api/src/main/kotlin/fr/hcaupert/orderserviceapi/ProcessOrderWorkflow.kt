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
    fun getOrder(): Order

    @SignalMethod
    fun markPaymentAsAuthorized()

    @SignalMethod
    fun shippingPrepared(trackingNumber: String)

    @SignalMethod
    fun orderShipped()
}

