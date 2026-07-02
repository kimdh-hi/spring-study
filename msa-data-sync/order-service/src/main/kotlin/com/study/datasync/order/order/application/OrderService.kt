package com.study.datasync.order.order.application

import com.study.datasync.order.order.domain.Order
import com.study.datasync.order.order.domain.OrderRepository
import com.study.datasync.order.order.domain.OutboxEvent
import com.study.datasync.order.order.domain.OutboxRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import tools.jackson.databind.ObjectMapper

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  private val outboxRepository: OutboxRepository,
  private val objectMapper: ObjectMapper,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Transactional
  fun create(product: String, amount: Int): Order {
    val order = orderRepository.save(Order.of(product, amount))

    val payload = objectMapper.writeValueAsString(
      mapOf("orderId" to order.id, "product" to order.product, "amount" to order.amount),
    )
    outboxRepository.save(OutboxEvent.of("Order", order.id!!, "OrderCreated", payload))

    log.info("order created. id={}, product={}, amount={}", order.id, product, amount)
    return order
  }

  @Transactional(readOnly = true)
  fun findAll(): List<Order> = orderRepository.findAll()
}
