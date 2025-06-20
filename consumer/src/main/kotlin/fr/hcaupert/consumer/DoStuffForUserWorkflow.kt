package fr.hcaupert.consumer

import io.temporal.workflow.SignalMethod
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import java.util.SortedSet

@WorkflowInterface
interface DoStuffForUserWorkflow {
    @WorkflowMethod
    fun start(events: SortedSet<Event>)

    @SignalMethod
    fun receiveEvent(event: Event)
}


//<editor-fold desc="Event">
data class Event(val eventId: Int, val userId: Int): Comparable<Event> {
    override fun compareTo(other: Event) = eventId compareTo other.eventId
}
//</editor-fold>
