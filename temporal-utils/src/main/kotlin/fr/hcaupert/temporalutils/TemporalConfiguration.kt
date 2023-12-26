package fr.hcaupert.temporalutils

import com.fasterxml.jackson.databind.ObjectMapper
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.worker.WorkerFactory
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Configuration
@Import(JacksonAutoConfiguration::class)
class TemporalConfiguration {

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs = WorkflowServiceStubs.newLocalServiceStubs()

    @Bean
    fun workflowClient(service: WorkflowServiceStubs, objectMapper: ObjectMapper): WorkflowClient {
        val options = WorkflowClientOptions {
            objectMapper.asDataConverter()
                .also(::setDataConverter)
        }
        return WorkflowClient.newInstance(service, options)
    }

    @Bean
    fun factory(client: WorkflowClient): WorkerFactory = WorkerFactory.newInstance(client)

    @Component
    class FactoryStarter(
        private val factory: WorkerFactory,
    ) {
        @EventListener
        fun startFactory(event: ContextRefreshedEvent) = factory.start()
    }

    companion object {
        fun ObjectMapper.asDataConverter(): DefaultDataConverter = DefaultDataConverter
            .newDefaultInstance()
            .withPayloadConverterOverrides(JacksonJsonPayloadConverter(this))
    }
}
