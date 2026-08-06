package com.study.sqsconsumer.domain.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "processed_orders")
class ProcessedOrder(
  @Id
  val orderId: Long,
  val productName: String,
  val quantity: Int,
  val customerId: String,
  val processedAt: Instant = Instant.now(),
) {
  companion object {
    fun from(message: OrderMessage) = ProcessedOrder(
      orderId = message.orderId,
      productName = message.productName,
      quantity = message.quantity,
      customerId = message.customerId,
    )
  }
}
