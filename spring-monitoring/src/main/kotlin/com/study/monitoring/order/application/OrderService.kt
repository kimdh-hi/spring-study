package com.study.monitoring.order.application

import com.study.monitoring.order.domain.Order
import com.study.monitoring.order.domain.repository.OrderRepository
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
  private val orderRepository: OrderRepository,
  meterRegistry: MeterRegistry,
) {
  private val log = LoggerFactory.getLogger(javaClass)

  // Custom metric -> exposed at /actuator/prometheus -> scraped by Prometheus -> stored in Mimir
  private val createdCounter: Counter = Counter.builder("orders.created")
    .description("number of created orders")
    .register(meterRegistry)

  @Transactional
  fun create(product: String, amount: Int): Order {
    val saved = orderRepository.save(Order.of(product, amount))
    createdCounter.increment()
    // traceId/spanId are injected into MDC by Micrometer Tracing -> shown in Loki for trace<->log correlation
    log.info("order created. id={}, product={}, amount={}", saved.id, product, amount)
    return saved
  }

  @Transactional(readOnly = true)
  fun findAll(): List<Order> = orderRepository.findAll()
}
