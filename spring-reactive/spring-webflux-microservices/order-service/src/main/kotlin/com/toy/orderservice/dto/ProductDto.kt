package com.toy.orderservice.dto

data class ProductDto(
  val id: String? = null,
  val description: String,
  val price: Int
)