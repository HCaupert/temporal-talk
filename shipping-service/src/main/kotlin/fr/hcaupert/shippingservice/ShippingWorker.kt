package fr.hcaupert.shippingservice

import fr.hcaupert.shippingserviceapi.ShippingService
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalWorker
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class ShippingWorker(
    workerFactory: WorkerFactory,
    shippingService: ShippingService,
) : MyTemporalWorker(
    queue = MyTemporalQueue.SHIPPING,
    activityImplementations = listOf(shippingService),
    workflowImplementationTypes = listOf(),
    factory = workerFactory,
)
