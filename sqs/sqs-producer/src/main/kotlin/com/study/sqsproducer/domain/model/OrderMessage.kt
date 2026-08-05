package com.study.sqsproducer.domain.model

import java.time.Instant

data class OrderMessage(
  val orderId: Long,
  val productName: String,
  val quantity: Int,
  val customerId: String,
  val createdAt: Instant,
) {
  companion object {
    fun from(order: Order) = OrderMessage(
      orderId = order.id,
      productName = order.productName,
      quantity = order.quantity,
      customerId = order.customerId,
      createdAt = order.createdAt,
    )
  }
}
