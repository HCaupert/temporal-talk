package fr.hcaupert.gateway

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.shippingserviceapi.ShippingService
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
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

    private val worker = workerFactory.newWorker("order-queue")
        .apply {
            registerWorkflowImplementationTypes(SimpleOrderWorkflowImpl::class.java)
            registerActivitiesImplementations(inventoryService, shippingService)
        }

    @PostConstruct()
    fun runWorkflowOnStartup() {
        val order = randomCreator.order()

        val options = WorkflowOptions {
            setWorkflowId("order-" + order.id)
            setTaskQueue("order-queue")
        }
        val processOrderWorkflow = workflowClient.newWorkflowStub(SimpleOrderWorkflow::class.java, options)

        WorkflowClient.start(processOrderWorkflow::processOrder, order)
    }
}
