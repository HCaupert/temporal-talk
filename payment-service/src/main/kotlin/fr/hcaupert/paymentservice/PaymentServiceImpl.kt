package fr.hcaupert.paymentservice

import fr.hcaupert.paymentserviceapi.PaymentCaptureId
import fr.hcaupert.paymentserviceapi.PaymentService
import fr.hcaupert.temporalutils.Failer
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class PaymentServiceImpl : PaymentService {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val failer = Failer(on = 3)

    override fun capturePayment(id: PaymentCaptureId) {
        failer.run()
        logger.info("\nPayment captured: \nID: $id")
    }
}
