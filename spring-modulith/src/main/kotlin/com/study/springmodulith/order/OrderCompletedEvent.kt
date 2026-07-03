package com.study.springmodulith.order

data class OrderCompletedEvent(
  val orderId: String,
  val product: String,
)
