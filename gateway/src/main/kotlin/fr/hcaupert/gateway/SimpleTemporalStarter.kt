package fr.hcaupert.gateway

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.shippingserviceapi.ShippingService
import io.temporal.client.WorkflowClient
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Service

@Service
class SimpleTemporalStarter(
    workerFactory: WorkerFactory,
    private val workflowClient: WorkflowClient,
    private val randomCreator: RandomCreator,
    private val shippingService: ShippingService,
    private val inventoryService: InventoryService,
) {

    @PostConstruct()
    fun runWorkflowOnStartup() {
        val order = randomCreator.order()

    }
}
