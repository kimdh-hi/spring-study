package com.toy.springmicrometer_grafana_prometheus.order.v1

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class OrderServiceV1(
  private val registry: MeterRegistry
): OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val stock = AtomicInteger(100)

  override fun order() {
    log.info("order...")
    stock.decrementAndGet()

    Counter.builder("my.order")
      .tag("class", this::class.java.name)
      .tag("method", "order")
      .description("order")
      .register(registry)
      .increment()
  }

  override fun cancel() {
    log.info("cancel...")
    stock.incrementAndGet()

    Counter.builder("my.order")
      .tag("class", this::class.java.name)
      .tag("method", "cancel")
      .description("cancel")
      .register(registry)
      .increment()
  }

  override fun getStock(): AtomicInteger {
    return stock
  }
}