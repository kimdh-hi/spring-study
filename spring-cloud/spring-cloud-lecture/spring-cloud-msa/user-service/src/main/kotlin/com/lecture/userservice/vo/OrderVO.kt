package com.lecture.userservice.vo

import java.io.Serial
import java.io.Serializable

data class OrderResponseVO(
  val orderId: String?,
  val userId: String,
  val productId: String,
  val quantity: Int,
  val unitPrice: Int,
  val totalPrice: Int,
)