package fr.hcaupert.consumer

import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface DoStuffActivity {
    @ActivityMethod(name = "DoStuffActivity-ForOneUser")
    fun forOneUser(events: Set<Event>)
}

class DoStuffActivityImpl : DoStuffActivity {
    override fun forOneUser(events: Set<Event>) {
        println("Doing stuff with ${events.size} events")
    }
}
