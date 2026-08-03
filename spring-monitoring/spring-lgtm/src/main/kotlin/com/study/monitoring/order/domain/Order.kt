package com.study.monitoring.order.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "orders")
class Order private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 200, nullable = false)
  var product: String,

  @Column(nullable = false)
  var amount: Int,
) {
  companion object {
    fun of(product: String, amount: Int) = Order(
      product = product,
      amount = amount,
    )
  }
}
