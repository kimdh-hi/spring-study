package com.toy.springmicrometer_grafana_prometheus.order.v0

import com.toy.springmicrometer_grafana_prometheus.order.OrderService
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicInteger

class OrderServiceV0: OrderService {

  private val log = LoggerFactory.getLogger(javaClass)

  private val stock = AtomicInteger(100)

  override fun order() {
    log.info("order...")
    stock.decrementAndGet()
  }

  override fun cancel() {
    log.info("cancel...")
    stock.incrementAndGet()
  }

  override fun getStock(): AtomicInteger {
    return stock
  }
}