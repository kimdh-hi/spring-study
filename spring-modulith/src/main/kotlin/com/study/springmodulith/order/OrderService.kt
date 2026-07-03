package com.study.springmodulith.order

import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class OrderService(
  private val events: ApplicationEventPublisher,
) {

  @Transactional
  fun placeOrder(product: String): Order {
    val order = Order(id = UUID.randomUUID().toString(), product = product)
    events.publishEvent(OrderCompletedEvent(orderId = order.id, product = order.product))
    return order
  }
}
