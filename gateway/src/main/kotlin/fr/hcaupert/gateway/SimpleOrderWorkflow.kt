package fr.hcaupert.gateway

import fr.hcaupert.orderserviceapi.Order
import io.temporal.workflow.QueryMethod
import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod

@WorkflowInterface
interface SimpleOrderWorkflow {
    @WorkflowMethod
    fun processOrder(order: Order)

    @SignalMethod
    fun orderPaid()

    @QueryMethod
    fun getOrder(): Order
}
