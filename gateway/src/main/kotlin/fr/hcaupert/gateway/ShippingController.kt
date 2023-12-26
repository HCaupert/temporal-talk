package fr.hcaupert.gateway

import ShippingId
import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.temporal.client.WorkflowClient
import io.temporal.client.newWorkflowStub
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("orders/{orderId}")
@Tag(name = "Shipping Service")
class ShippingController(
    private val workflowClient: WorkflowClient,
) {
    @PostMapping("/prepared-shippings")
    @Operation(summary = "Mark an order prepared")
    fun markOrderAsPrepared(@PathVariable orderId: String, @RequestBody shipping: ShippingId) {
        workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId)
            .shippingPrepared(shipping.id)
    }

    @PostMapping("/shippings")
    @Operation(summary = "Mark an order shipped")
    fun markOrderAsShipped(@PathVariable orderId: String) {
        workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId)
            .orderShipped()
    }
}
