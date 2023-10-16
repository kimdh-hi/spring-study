package com.toy.springmicrometer_grafana_prometheus.order.v4

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.MeterRegistry
import io.micrometer.core.instrument.Timer
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random

// Timer
// count, total_time, max 매트릭 수집
// total_time 의 경우 1~3분 주기마다 갱신
@Timed("my.order")
class OrderServiceV4: OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val stock = AtomicInteger(100)

  override fun order() {
    log.info("order...")
    stock.decrementAndGet()
    Thread.sleep(500 + Random.nextLong(200))
  }

  override fun cancel() {
    log.info("cancel...")
    stock.incrementAndGet()
    Thread.sleep(200 + Random.nextLong(200))
  }

  override fun getStock(): AtomicInteger {
    return stock
  }
}