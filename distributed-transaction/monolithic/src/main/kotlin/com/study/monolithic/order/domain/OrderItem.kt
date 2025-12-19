package com.study.monolithic.order.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "order_items")
class OrderItem private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  val orderId: Long,

  val productId: Long,

  val quantity: Long,
) {

  companion object {
    fun of(orderId: Long, productId: Long, quantity: Long): OrderItem {
      return OrderItem(orderId = orderId, productId = productId, quantity = quantity)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as OrderItem

    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }
}
