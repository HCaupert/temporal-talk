package fr.hcaupert.temporalutils

import io.temporal.activity.ActivityOptions
import io.temporal.common.SearchAttributeKey
import io.temporal.common.SearchAttributeUpdate
import io.temporal.workflow.Workflow
import java.time.Duration

object MyTemporalUtils {
    enum class MySearchAttributes(key: String) {
        SHIPPING_STATUS("ShippingStatus"),
        ;

        // Not search attribute type proof (only keyword)
        val key: SearchAttributeKey<String> = SearchAttributeKey.forKeyword(key)
    }

    fun mySetSearchAttribute(searchAttributes: MySearchAttributes, value: Any) {
        searchAttributes.key
            .let { SearchAttributeUpdate.valueSet(it, value.toString()) }
            .let { Workflow.upsertTypedSearchAttributes(it) }
    }

    inline fun <reified T> myActivity(queue: MyTemporalQueue): T = ActivityOptions {
        setTaskQueue(queue.name)
        setStartToCloseTimeout(Duration.ofSeconds(10))
    }
        .let { Workflow.newActivityStub(T::class.java, it) }
}
