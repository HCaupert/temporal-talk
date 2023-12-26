package fr.hcaupert.inventoryservice

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.temporalutils.MyTemporalQueue
import fr.hcaupert.temporalutils.MyTemporalWorker
import io.temporal.worker.WorkerFactory
import org.springframework.stereotype.Service

@Service
class InventoryWorker(
    workerFactory: WorkerFactory,
    inventoryService: InventoryService,
) : MyTemporalWorker(
    queue = MyTemporalQueue.INVENTORY,
    factory = workerFactory,
    activityImplementations = listOf(inventoryService)
)
