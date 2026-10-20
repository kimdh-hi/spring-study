package com.toy.warmup.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Product(
  var name: String,
  var category: String,
  var price: Long,
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  val id: Long = 0,
) {

  fun update(name: String, category: String, price: Long) {
    this.name = name
    this.category = category
    this.price = price
  }
}
