package com.lecture.productservice.entity

import org.springframework.data.annotation.Id

class Product(
  @Id
  var id: String,
  var description: String,
  var price: Int
) {
  fun update(description: String, price: Int) {
    this.description = description
    this.price = price
  }
}