package com.study.monitoring.order.application

import com.study.monitoring.order.domain.Order
import com.study.monitoring.order.domain.repository.OrderRepository
import io.micrometer.core.annotation.Counted
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val orderRepository: OrderRepository,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  @Counted(value = "orders.created", description = "number of created orders")
  @Transactional
  fun create(product: String, amount: Int): Order {
    val saved = orderRepository.save(Order.of(product, amount))
    log.info("order created. id={}, product={}, amount={}", saved.id, product, amount)
    return saved
  }

  @Transactional(readOnly = true)
  fun findAll(): List<Order> = orderRepository.findAll()
}
