package com.study.concurrency.stock.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version

@Entity
@Table(name = "optimistic_stock")
class OptimisticStock private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  @Column(nullable = false)
  var productId: Long,

  @Column(nullable = false)
  var quantity: Long,

  @Version
  var version: Long? = null,
) {
  fun decrease(amount: Long) {
    require(amount > 0) { "amount must be positive. amount=$amount" }
    require(quantity >= amount) { "stock is not enough. quantity=$quantity, amount=$amount" }
    quantity -= amount
  }

  companion object {
    fun of(productId: Long, quantity: Long) = OptimisticStock(
      productId = productId,
      quantity = quantity,
    )
  }
}
