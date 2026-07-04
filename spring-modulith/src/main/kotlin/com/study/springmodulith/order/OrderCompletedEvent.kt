package com.study.springmodulith.order

import org.springframework.modulith.events.Externalized

@Externalized("order.completed::#{orderId}")
data class OrderCompletedEvent(
  val orderId: String,
  val product: String,
)
