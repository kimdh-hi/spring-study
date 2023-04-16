package com.lecture.productservice.dto

import com.lecture.productservice.entity.Product

data class ProductDto(
  val id: String,
  val description: String,
  val price: Int
) {
  companion object {
    fun of(product: Product) = ProductDto(
      id = product.id,
      description = product.description,
      price = product.price
    )
  }

  fun toEntity(): Product = Product(
    id = this.id,
    description = this.description,
    price = this.price
  )
}