package fr.hcaupert.gateway

import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import io.swagger.v3.oas.annotations.Hidden
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import io.temporal.client.WorkflowClient
import io.temporal.client.newWorkflowStub
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@Tag(name = "Payment Service")
@RequestMapping("/psps/payment-confirmations")
@CrossOrigin
class PaymentController(
    private val workflowClient: WorkflowClient,
) {

    @Hidden
    @PostMapping("/{orderId}")
    fun receivePaymentNotification(
        @PathVariable
        @Schema(description = "The corresponding order id")
        orderId: UUID,
    ) {
        workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId.toString())
            .markPaymentAsAuthorized()
    }
}
