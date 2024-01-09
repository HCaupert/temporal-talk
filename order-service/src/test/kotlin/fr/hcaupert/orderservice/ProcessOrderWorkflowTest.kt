//package fr.hcaupert.orderservice
//
//import com.fasterxml.jackson.databind.DeserializationFeature
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
//import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
//import fr.hcaupert.inventoryserviceapi.InventoryService
//import fr.hcaupert.orderserviceapi.*
//import fr.hcaupert.paymentserviceapi.PaymentService
//import fr.hcaupert.shippingserviceapi.ShippingService
//import fr.hcaupert.temporalutils.MyTemporalQueue
//import fr.hcaupert.temporalutils.MyTemporalUtils
//import fr.hcaupert.temporalutils.TemporalConfiguration.Companion.asDataConverter
//import io.temporal.client.WorkflowClient
//import io.temporal.client.WorkflowClientOptions
//import io.temporal.testing.TestWorkflowEnvironment
//import io.temporal.testing.TestWorkflowExtension
//import org.assertj.core.api.Assertions.assertThat
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.extension.RegisterExtension
//import org.mockito.kotlin.mock
//import org.mockito.kotlin.verify
//import java.time.LocalDateTime
//import java.util.*
//
//class ProcessOrderWorkflowTest {
//
//    private val shippingService: ShippingService = mock()
//    private val inventoryService: InventoryService = mock()
//    private val paymentService: PaymentService = mock()
//
//
//    @RegisterExtension
//    val temporalExtension: TestWorkflowExtension = TestWorkflowExtension.newBuilder()
//        .setWorkflowTypes(ProcessOrderWorkflowImpl::class.java)
//        .setDoNotStart(true)
//        .makeSerializationWorkWithKotlinAndRegisterShippingStatusSearchAttribute()
//        .build()
//
//    @Test
//    fun `i'm unit testing my workflow!`(env: TestWorkflowEnvironment, workflow: ProcessOrderWorkflow) {
//        env.newWorker(MyTemporalQueue.SHIPPING.name)
//            .registerActivitiesImplementations(shippingService)
//        env.newWorker(MyTemporalQueue.INVENTORY.name)
//            .registerActivitiesImplementations(inventoryService)
//        env.newWorker(MyTemporalQueue.PAYMENT.name)
//            .registerActivitiesImplementations(paymentService)
//        env.start()
//
//        WorkflowClient.start(workflow::processOrder, order)
//
//        // Setup
//        val setupOrder = workflow.getOrder()
//        assertThat(setupOrder.payment.status).isEqualTo(PaymentStatus.PENDING)
//        assertThat(setupOrder.shipping.status).isEqualTo(ShippingStatus.PENDING)
//
//        // Inventory
//        verify(inventoryService).reserveArticle(order.article.id)
//
//        // Payment Authorization
//        workflow.markPaymentAsAuthorized()
//        assertThat(workflow.getOrder().shipping.status).isEqualTo(ShippingStatus.AWAITING_PREPARATION)
//
//        // Prepare Shipping
//        workflow.shippingPrepared(trackingNumber)
//        val preparedOrder = workflow.getOrder()
//        assertThat(preparedOrder.shipping.status).isEqualTo(ShippingStatus.PREPARED)
//        assertThat(preparedOrder.shipping.trackingNumber).isEqualTo(trackingNumber)
//
//        // Payment capture
//        verify(paymentService).capturePayment(order.payment.id)
//        val paidOrder = workflow.getOrder()
//        assertThat(paidOrder.payment.status).isEqualTo(PaymentStatus.CAPTURED)
//        assertThat(paidOrder.shipping.status).isEqualTo(ShippingStatus.AWAITING_SHIPPING)
//
//        // Shipping
//        workflow.orderShipped()
//        assertThat(workflow.getOrder().shipping.status).isEqualTo(ShippingStatus.SHIPPED)
//    }
//}
//
//const val trackingNumber = "DJ38IDJHCBZER"
//
//val order = Order(
//    id = UUID.randomUUID(),
//    creationDate = LocalDateTime.now(),
//    article = Article(
//        id = UUID.randomUUID(),
//        image = "http://image.game/fun",
//        name = "Super fun game",
//        price = 1234,
//    ),
//    payment = Payment(id = UUID.randomUUID()),
//    shipping = ShippingDetails(
//        method = ShippingDetails.ShippingMethod.random(),
//        address = Address(
//            street = "Rue du bonheur",
//            city = "Paris",
//            iso3CountryCode = "FRA",
//        ),
//        receiver = Receiver(
//            firstName = "John",
//            lastName = "Doe",
//        ),
//    )
//)
//
//fun TestWorkflowExtension.Builder.makeSerializationWorkWithKotlinAndRegisterShippingStatusSearchAttribute(): TestWorkflowExtension.Builder =
//    setWorkflowClientOptions(
//        WorkflowClientOptions {
//            registerSearchAttribute(
//                MyTemporalUtils.MySearchAttributes.SHIPPING_STATUS.key.name,
//                MyTemporalUtils.MySearchAttributes.SHIPPING_STATUS.key.valueType
//            )
//            jacksonObjectMapper()
//                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//                .registerModules(JavaTimeModule())
//                .asDataConverter()
//                .also(::setDataConverter)
//        }
//    )
