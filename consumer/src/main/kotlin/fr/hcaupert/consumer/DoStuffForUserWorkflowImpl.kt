package fr.hcaupert.consumer

import io.temporal.activity.ActivityOptions
import io.temporal.workflow.Workflow
import java.time.Duration
import java.util.*

class DoStuffForUserWorkflowImpl : DoStuffForUserWorkflow {

    private var events = sortedSetOf<Event>()
    private val stepDuration = Duration.ofDays(1)

    private val doStuffActivity = Workflow.newActivityStub(
        DoStuffActivity::class.java,
        ActivityOptions { setStartToCloseTimeout(Duration.ofSeconds(10)) }
    )

    override fun start(events: SortedSet<Event>) {
        Workflow.sleep(stepDuration)

        //<editor-fold>
        val currentEvents = this.events + events
        this.events = sortedSetOf()
        //</editor-fold>

        doStuffActivity.forOneUser(currentEvents)

        //<editor-fold>
        if (events.isNotEmpty()) {
            Workflow.continueAsNew(events)
        }
        //</editor-fold>
    }

    override fun receiveEvent(event: Event) {
        events.add(event)
    }
}
