package fr.hcaupert.gateway

import OrderListElement
import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.temporalutils.MyTemporalUtils.MySearchAttributes
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowExecutionMetadata
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

        return order.id
    }

    @Hidden
    @GetMapping
    fun listOrders(): Stream<OrderListElement> {
        return Stream.of()
    }

    @Hidden
    @GetMapping("/{orderId}")
    fun getOrder(@PathVariable orderId: String): Order {
        TODO("Fix me later")
    }
}

private fun WorkflowExecutionMetadata.mySearchAttribute(searchAttribute: MySearchAttributes): String? {
    return searchAttribute.key
        .takeIf(typedSearchAttributes::containsKey)
        ?.let(typedSearchAttributes::get)
}
