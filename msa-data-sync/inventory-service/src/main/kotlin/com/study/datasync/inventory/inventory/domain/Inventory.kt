package com.study.datasync.inventory.inventory.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.UuidGenerator

@Entity
@Table(name = "inventory")
class Inventory private constructor(
  @Id
  @UuidGenerator
  @Column(length = 50)
  var id: String? = null,

  @Column(length = 200, nullable = false, unique = true)
  var product: String,

  @Column(nullable = false)
  var quantity: Int,
) {
  fun decrease(amount: Int) {
    this.quantity -= amount
  }

  companion object {
    fun of(product: String, quantity: Int) = Inventory(
      product = product,
      quantity = quantity,
    )
  }
}
