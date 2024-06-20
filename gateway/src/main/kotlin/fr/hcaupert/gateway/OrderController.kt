package fr.hcaupert.gateway

import OrderListElement
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import fr.hcaupert.orderserviceapi.ShippingStatus
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalUtils.MySearchAttributes
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowExecutionMetadata
import io.temporal.client.WorkflowOptions
import io.temporal.client.newWorkflowStub
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.stream.Stream

@CrossOrigin
@RestController
@RequestMapping("orders")
@Tag(name = "Order Service")
class OrderController(
    private val workflowClient: WorkflowClient,
    private val randomCreator: RandomCreator,
) {
    @PostMapping
    @Operation(summary = "Create an order.", description = "If an empty body is sent, a random order is created.")
    fun createOrder(@RequestBody order: Order = randomCreator.order()): UUID {
        val options = WorkflowOptions {
            setWorkflowId(order.id.toString())
            setTaskQueue(MyTemporalQueue.ORDER.name)
        }
        val workflow: ProcessOrderWorkflow = workflowClient.newWorkflowStub(options)
        WorkflowClient.start(workflow::processOrder, order)
        return order.id
    }

    @Hidden
    @GetMapping
    fun listOrders(): Stream<OrderListElement> {
        return workflowClient.listExecutions("WorkflowType=\"ProcessOrderWorkflow\"")
            .map { wf ->
                OrderListElement(
                    id = UUID.fromString(wf.execution.workflowId),
                    date = wf.executionTime,
                    shippingStatus = ShippingStatus.valueOfOrPending(wf.mySearchAttribute(MySearchAttributes.SHIPPING_STATUS)),
                )
            }
    }

    @Hidden
    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String): Order {
        val processOrderWorkflow = workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId)
        return processOrderWorkflow.getOrder()
    }
}

private fun WorkflowExecutionMetadata.mySearchAttribute(searchAttribute: MySearchAttributes): String? {
    return searchAttribute.key
        .takeIf(typedSearchAttributes::containsKey)
        ?.let(typedSearchAttributes::get)
}
