package com.toy.orderservice.dto

data class PurchaseOrderRequestDto(
  val userId: Int,
  val productId: Int,
)

data class PurchaseOrderResponseDto(
  val orderId: Int,
  val userId: Int,
  val productId: Int,

  val amount: Int,
  val status: OrderStatus
)

enum class OrderStatus {
  COMPLETED, FAILED
}