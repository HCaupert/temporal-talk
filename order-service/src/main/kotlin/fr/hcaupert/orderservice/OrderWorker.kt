package fr.hcaupert.orderservice

import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalWorker
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class OrderWorker(
    workerFactory: WorkerFactory,
) : MyTemporalWorker(
    queue = MyTemporalQueue.ORDER,
    workflowImplementationTypes = listOf(ProcessOrderWorkflowImpl::class),
    factory = workerFactory,
)
