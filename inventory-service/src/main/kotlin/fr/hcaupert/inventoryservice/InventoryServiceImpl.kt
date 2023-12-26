package fr.hcaupert.inventoryservice

import fr.hcaupert.inventoryserviceapi.InventoryService
import fr.hcaupert.temporalutils.Failer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*

@Service
class InventoryServiceImpl : InventoryService {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val failer = Failer(on = 3)

    override fun reserveArticle(id: UUID) {
        failer.run()
        val newInventory = inventoryById(id) - 1
        if (newInventory < 0) throw RuntimeException("This article is not in stock anymore...")
        logStatus("reserved", id, newInventory)
        save(id, newInventory)
    }

    override fun releaseArticle(id: UUID) {
        failer.run()
        val newInventory = inventoryById(id) + 1
        logStatus("released", id, newInventory)
        save(id, newInventory)
    }

    private fun logStatus(action: String, id: UUID, count: Int) = logger.info(
        "\n" +
                "Article $action:\n" +
                "ID: $id\n" +
                "Left in stock: $count"
    )

    /*
     Mocked DAO
     */
    private val startingInventory = 10
    private val articleInventories = mutableMapOf<UUID, Int>()
    private fun inventoryById(id: UUID) = articleInventories.getOrPut(id) { startingInventory }
    private fun save(id: UUID, count: Int) = articleInventories.set(id, count)
}
