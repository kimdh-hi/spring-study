package com.study.sqsconsumer.domain.model

import java.time.Instant

data class OrderMessage(
  val orderId: Long,
  val productName: String,
  val quantity: Int,
  val customerId: String,
  val createdAt: Instant,
)
