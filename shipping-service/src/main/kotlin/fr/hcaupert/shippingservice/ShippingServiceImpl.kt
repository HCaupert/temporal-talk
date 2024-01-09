package fr.hcaupert.shippingservice

import fr.hcaupert.orderserviceapi.Order
import fr.hcaupert.shippingserviceapi.ShippingService
import fr.hcaupert.temporalutils.Failer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ShippingServiceImpl : ShippingService {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val failer = Failer(on = 3)

    override fun shipOrder(order: Order) {
        failer.run()
        logger.info("\n" +
                "Order shipped:\n" +
                "ID: ${order.id}\n" +
                "To: ${order.shipping.receiver.firstName} ${order.shipping.receiver.lastName}\n" +
                "Delivery: ${order.shipping.method}")
    }
}
