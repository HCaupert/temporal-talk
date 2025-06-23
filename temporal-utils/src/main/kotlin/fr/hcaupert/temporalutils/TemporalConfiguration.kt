package fr.hcaupert.temporalutils

import com.fasterxml.jackson.databind.ObjectMapper
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.common.converter.DefaultDataConverter
import io.temporal.common.converter.JacksonJsonPayloadConverter
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.WorkerFactory
import org.springframework.beans.factory.annotation.Value
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

    @Value("\${app.temporal-api-key}")
    lateinit var apiKey: String

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs = WorkflowServiceStubs.newServiceStubs(WorkflowServiceStubsOptions {
        addApiKey { apiKey }
        setEnableHttps(true)
        setTarget("eu-west-2.aws.api.temporal.io:7233")
    })

    @Bean
    fun workflowClient(service: WorkflowServiceStubs, objectMapper: ObjectMapper): WorkflowClient {
        val options = WorkflowClientOptions {
            objectMapper.asDataConverter()
                .also(::setDataConverter)
            setNamespace("hugo.nxuww")
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
