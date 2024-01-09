package fr.hcaupert.shippingservice

import fr.hcaupert.shippingserviceapi.ShippingService
import fr.hcaupert.temporalutils.MyTemporalQueue
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class ShippingWorker(
    workerFactory: WorkerFactory,
    shippingService: ShippingService,
) {
    val worker = workerFactory.newWorker(MyTemporalQueue.SHIPPING.name)
        .registerActivitiesImplementations()
}
