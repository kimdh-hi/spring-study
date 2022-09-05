package com.toy.productservice.domain

import org.springframework.data.annotation.Id

data class Product(
  @Id
  val id: String,
  val description: String,
  val price: Int
)