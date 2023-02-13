package com.toy.emailservice.vo

import com.toy.emailservice.config.NoArg

@NoArg
data class OrderEvent(
  val status: OrderStatus,
  val message: String,
  val order: Order,
)

enum class OrderStatus {
  PENDING, COMPLETE
}