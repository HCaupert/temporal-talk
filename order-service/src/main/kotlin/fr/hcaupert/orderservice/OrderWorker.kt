package fr.hcaupert.orderservice

import fr.hcaupert.temporalutils.MyTemporalQueue
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class OrderWorker(
    workerFactory: WorkerFactory,
) {
    // TODO create the shipping worker !
//    val worker = workerFactory.newWorker(MyTemporalQueue..name)
//        .registerWorkflowImplementationTypes()
}
