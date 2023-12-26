package fr.hcaupert.temporalutils

import io.temporal.worker.WorkerFactory
import kotlin.reflect.KClass

enum class MyTemporalQueue {
    INVENTORY,
    ORDER,
    PAYMENT,
    SHIPPING,
}

abstract class MyTemporalWorker(
    queue: MyTemporalQueue,
    factory: WorkerFactory,
    activityImplementations: List<Any> = listOf(),
    workflowImplementationTypes: List<KClass<*>> = listOf(),
) {
    init {
        val worker = factory.newWorker(queue.name)
        worker.registerWorkflowImplementationTypes(*workflowImplementationTypes.map(KClass<*>::java).toTypedArray())
        worker.registerActivitiesImplementations(*activityImplementations.toTypedArray())
    }
}
