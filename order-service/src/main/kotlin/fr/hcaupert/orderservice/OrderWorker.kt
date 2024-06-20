package fr.hcaupert.orderservice

import fr.hcaupert.temporalutils.MyTemporalQueue
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class OrderWorker(
    workerFactory: WorkerFactory,
) {
    val worker = workerFactory.newWorker(MyTemporalQueue.ORDER.name)
        .registerWorkflowImplementationTypes(ProcessOrderWorkflowImpl::class.java)
}
