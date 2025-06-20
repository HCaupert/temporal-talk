package fr.hcaupert.consumer

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import fr.hcaupert.temporalutils.TemporalConfiguration.Companion.asDataConverter
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.client.WorkflowOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.WorkerFactory
import java.util.concurrent.Executors

//<editor-fold desc="Config boilerplate">
val workflowServiceStubs = WorkflowServiceStubs.newLocalServiceStubs()
val workflowClient = WorkflowClient.newInstance(workflowServiceStubs, WorkflowClientOptions {
    jacksonObjectMapper().asDataConverter().also(::setDataConverter)
})
val workerFactory = WorkerFactory.newInstance(workflowClient)
val queue = "kafka-loves-temporal-and-temporal-loves-it"

val worker = workerFactory.newWorker(queue).apply {
    registerActivitiesImplementations(DoStuffActivityImpl())
    registerWorkflowImplementationTypes(DoStuffForUserWorkflowImpl::class.java)
    workerFactory.start()
}

fun simulate50EventsFor3Users(runnable: (i: Event) -> Unit) {
    Executors.newVirtualThreadPerTaskExecutor().use { executor ->
        for (u in 1..<4) {
            for (i in 1..<51) {
                executor.submit { runnable(Event(i, u)) }.get()
            }
        }
    }
}
//</editor-fold>

fun main() = simulate50EventsFor3Users { event ->
    val workflow = workflowClient.newWorkflowStub(
        DoStuffForUserWorkflow::class.java,
        WorkflowOptions {
            setTaskQueue(queue)
            setWorkflowId("doing-stuff-for-user-${event.userId}")
        })

    val signalWithStart = workflowClient.newSignalWithStartRequest()
    signalWithStart.add(workflow::start, sortedSetOf())
    signalWithStart.add(workflow::receiveEvent, event)
    workflowClient.signalWithStart(signalWithStart)
}
