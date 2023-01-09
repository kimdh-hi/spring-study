package com.lecture.orderservice.vo

import com.lecture.orderservice.domain.Order
import java.io.Serial
import java.io.Serializable


data class OrderSaveRequestVO(
  val productId: String,
  val quantity: Int,
  val unitPrice: Int,
): Serializable {

  private var totalPrice: Int = 0

  lateinit var userId: String

  companion object {
    @Serial
    private const val serialVersionUID: Long = 3259315572800818515L
  }

  fun calculateTotalPrice() {
    this.totalPrice = quantity * unitPrice
  }

  fun toEntity() = Order(
    productId = productId,
    userId = userId,
    quantity = quantity,
    unitPrice = unitPrice,
    totalPrice = totalPrice
  )
}
data class OrderResponseVO(
  val orderId: String?,
  val userId: String,
  val productId: String,
  val quantity: Int,
  val unitPrice: Int,
  val totalPrice: Int,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -3063440302639312601L

    fun of(order: Order) = OrderResponseVO(
      orderId = order.id,
      userId = order.userId,
      productId = order.productId,
      quantity = order.quantity,
      unitPrice = order.unitPrice,
      totalPrice = order.totalPrice
    )
  }
}