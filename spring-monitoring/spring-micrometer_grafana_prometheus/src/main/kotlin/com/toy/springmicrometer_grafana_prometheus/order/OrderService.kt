package com.toy.springmicrometer_grafana_prometheus.order

import java.util.concurrent.atomic.AtomicInteger

interface OrderService {
  fun order()
  fun cancel()
  fun getStock(): AtomicInteger
}

