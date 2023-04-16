package com.lecture.dto

data class OrderRequestDto(
  val userId: String,
  val productId: String
)

data class OrderResponseDto(
  val orderId: String,
  val productId: String,
  val userId: String,
  val amount: Int,
  val status: OrderStatus
)