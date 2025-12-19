package com.study.monolithic.product.domian

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product private constructor(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,

  var quantity: Long,

  var price: Long,
) {

  fun calculatePrice(quantity: Long): Long {
    return price * quantity
  }

  fun buy(quantity: Long) {
    if (this.quantity < quantity) {
      throw RuntimeException("out of quantity.")
    }

    this.quantity -= quantity
  }

  companion object {
    fun of(quantity: Long, price: Long): Product {
      return Product(quantity = quantity, price = price)
    }
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as Product

    return id == other.id
  }

  override fun hashCode(): Int {
    return id?.hashCode() ?: 0
  }
}
