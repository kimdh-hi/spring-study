package com.toy.orderservice.vo

data class OrderEvent(
  val status: OrderStatus,
  val message: String,
  val order: Order,
)

enum class OrderStatus {
  PENDING, COMPLETE
}