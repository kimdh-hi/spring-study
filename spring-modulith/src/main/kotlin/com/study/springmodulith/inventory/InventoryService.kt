package com.study.springmodulith.inventory

import com.study.springmodulith.order.OrderCompletedEvent
import org.slf4j.LoggerFactory
import org.springframework.modulith.events.ApplicationModuleListener
import org.springframework.stereotype.Service

@Service
class InventoryService {

  private val logger = LoggerFactory.getLogger(InventoryService::class.java)

  @ApplicationModuleListener
  fun on(event: OrderCompletedEvent) {
    logger.info("Reducing stock for product '{}' (order {})", event.product, event.orderId)
    if (event.product == "fail") error("stock reduction failed for order ${event.orderId}")
  }
}
