import fr.hcaupert.orderserviceapi.ShippingStatus
import java.time.Instant
import java.util.*

data class OrderListElement(
    val id: UUID,
    val date: Instant,
    val shippingStatus: ShippingStatus,
)

data class ShippingId(
    val id: String,
)
