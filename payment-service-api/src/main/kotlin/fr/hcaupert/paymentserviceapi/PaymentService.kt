package fr.hcaupert.paymentserviceapi

import io.temporal.activity.ActivityInterface
import java.util.*

typealias PaymentCaptureId = UUID

@ActivityInterface
interface PaymentService {
    fun capturePayment(id: PaymentCaptureId)
}

