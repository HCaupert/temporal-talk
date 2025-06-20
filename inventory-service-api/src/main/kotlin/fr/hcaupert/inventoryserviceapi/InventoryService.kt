package fr.hcaupert.inventoryserviceapi

import io.temporal.activity.ActivityInterface
import java.util.*

@ActivityInterface
interface InventoryService {
    fun prepareArticle(id: UUID)
    fun releaseArticle(id: UUID)
}
