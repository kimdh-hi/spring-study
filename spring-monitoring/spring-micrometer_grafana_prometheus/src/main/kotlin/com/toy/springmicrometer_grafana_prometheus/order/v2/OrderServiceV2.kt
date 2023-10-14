package com.toy.springmicrometer_grafana_prometheus.order.v2

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import io.micrometer.core.annotation.Counted
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.MeterRegistry
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class OrderServiceV2: OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val stock = AtomicInteger(100)

  // tag 는 메서드 이름을 기준으로 분류
  @Counted("my.order")
  override fun order() {
    log.info("order...")
    stock.decrementAndGet()
  }

  @Counted("my.order")
  override fun cancel() {
    log.info("cancel...")
    stock.incrementAndGet()
  }

  override fun getStock(): AtomicInteger {
    return stock
  }
}