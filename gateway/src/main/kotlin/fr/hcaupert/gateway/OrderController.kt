package fr.hcaupert.gateway

import OrderListElement
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.orderserviceapi.OrderInfo
import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import fr.hcaupert.orderserviceapi.ShippingStatus
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalUtils.MySearchAttributes
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
    fun createOrder(@RequestBody(required = false) order: Order = randomCreator.order()): UUID {

        val options = WorkflowOptions {
            setTaskQueue(MyTemporalQueue.ORDER.name)
            setWorkflowId(order.id.toString())
        }
        val processOrderWorkflow = workflowClient.newWorkflowStub<ProcessOrderWorkflow>(options)
        WorkflowClient.start(processOrderWorkflow::processOrder, order)

        return order.id
    }

    @GetMapping
    @Operation(summary = "List orders", description = "List all orders in the app.")
    fun listOrders(): Stream<OrderListElement> {
        return workflowClient.listExecutions("")
            .map {
                OrderListElement(
                    id = UUID.fromString(it.execution.workflowId),
                    date = it.startTime,
                    shippingStatus = ShippingStatus.valueOfOrPending(it.mySearchAttribute(MySearchAttributes.SHIPPING_STATUS)),
                )
            }
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Find an order.", description = "Find an order by id.")
    fun getOrder(@PathVariable orderId: String): OrderInfo {
        return workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId)
            .getOrder()
    }
}

private fun WorkflowExecutionMetadata.mySearchAttribute(searchAttribute: MySearchAttributes): String? {
    return searchAttribute.key
        .takeIf(typedSearchAttributes::containsKey)
        ?.let(typedSearchAttributes::get)
}
