package com.study.datasync.inventory.inventory.application

import com.study.datasync.inventory.inventory.domain.Inventory
import com.study.datasync.inventory.inventory.domain.InventoryRepository
import com.study.datasync.inventory.inventory.domain.ProcessedEvent
import com.study.datasync.inventory.inventory.domain.ProcessedEventRepository
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class OrderEventListener(
  private val inventoryRepository: InventoryRepository,
  private val processedEventRepository: ProcessedEventRepository,
  private val objectMapper: ObjectMapper,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @KafkaListener(topics = ["datasync.event.Order"], groupId = "inventory-service")
  @Transactional
  fun handle(payload: String) {
    val event = objectMapper.readTree(payload)
    val orderId = event.get("orderId").asString()
    val product = event.get("product").asString()
    val amount = event.get("amount").asInt()

    if (processedEventRepository.existsById(orderId)) {
      log.info("duplicate event ignored. orderId={}", orderId)
      return
    }

    val inventory = inventoryRepository.findByProduct(product) ?: Inventory.of(product, 100)
    inventory.decrease(amount)
    inventoryRepository.save(inventory)
    processedEventRepository.save(ProcessedEvent(orderId))

    log.info("inventory synced. orderId={}, product={}, amount=-{}, remain={}", orderId, product, amount, inventory.quantity)
  }
}
