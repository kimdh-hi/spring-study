package com.toy.springmicrometer_grafana_prometheus.order.v3

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

// Timer
// count, total_time, max 매트릭 수집
class OrderServiceV3(
  private val registry: MeterRegistry
): OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val stock = AtomicInteger(100)

  override fun order() {
    val timer = Timer.builder("my.order")
      .tag("class", this::class.java.name)
      .tag("method", "oder")
      .description("order")
      .register(registry)

    timer.record<Unit> {
      log.info("order...")
      stock.decrementAndGet()
      Thread.sleep(500 + Random.nextLong(200))
    }
  }

  override fun cancel() {
    val timer = Timer.builder("my.order")
      .tag("class", this::class.java.name)
      .tag("method", "cancel")
      .description("cancel")
      .register(registry)

    timer.record<Unit> {
      log.info("cancel...")
      stock.incrementAndGet()
      Thread.sleep(200 + Random.nextLong(200))
    }
  }

  override fun getStock(): AtomicInteger {
    return stock
  }
}