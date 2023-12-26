package fr.hcaupert.gateway

import fr.hcaupert.orderserviceapi.ProcessOrderWorkflow
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.tags.Tag
import io.temporal.client.WorkflowClient
import io.temporal.client.newWorkflowStub
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Tag(name = "Payment Service")
@RequestMapping("/psps/payment-confirmations")
@CrossOrigin
class PaymentController(
    private val workflowClient: WorkflowClient,
) {

    @Operation(
        summary = "Receive a payment confirmation from PSP.",
        description = "This endpoints is where we receive webhook from payment providers.",
    )
    @PostMapping("/{orderId}")
    fun receivePaymentNotification(
        @PathVariable
        @Schema(description = "The corresponding order id")
        orderId: UUID,
    ) {
        workflowClient.newWorkflowStub<ProcessOrderWorkflow>(orderId.toString())
            .paymentAuthorizationReceived()
    }
}
