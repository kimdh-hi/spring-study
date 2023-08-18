package com.toy.jpanamedlock.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Stock(
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long = 0,

  var productId: String,

  var quantity: Long = 0
) {
  fun decrease(quantity: Long) {
    if (this.quantity - quantity < 0) {
      throw IllegalArgumentException("out of stock...")
    }

    println("decrease... quantity: $quantity" )
    this.quantity = this.quantity - quantity
  }
}