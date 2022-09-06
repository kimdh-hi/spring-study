package com.toy.productservice.domain

import org.springframework.data.annotation.Id

data class Product(
  @Id
  var id: String? = null,
  var description: String,
  var price: Int
) {
  fun update(description: String, price: Int) {
    this.description = description
    this.price = price
  }
}