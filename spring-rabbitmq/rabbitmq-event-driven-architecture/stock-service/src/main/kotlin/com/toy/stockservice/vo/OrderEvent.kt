package com.toy.stockservice.vo

import com.toy.stockservice.config.NoArg

@NoArg
data class OrderEvent(
  val status: OrderStatus,
  val message: String,
  val order: Order,
)

enum class OrderStatus {
  PENDING, COMPLETE
}