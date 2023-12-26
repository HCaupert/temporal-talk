package fr.hcaupert.paymentservice

import fr.hcaupert.paymentserviceapi.PaymentService
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalWorker
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class PaymentWorker(
    workerFactory: WorkerFactory,
    paymentService: PaymentService,
) : MyTemporalWorker(
    queue = MyTemporalQueue.PAYMENT,
    activityImplementations = listOf(paymentService),
    workflowImplementationTypes = listOf(),
    factory = workerFactory,
)
