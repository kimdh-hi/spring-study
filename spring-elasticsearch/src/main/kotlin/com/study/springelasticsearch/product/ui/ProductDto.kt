package com.study.springelasticsearch.product.ui

import com.study.springelasticsearch.product.domain.model.Product

data class ProductRegisterRequest(
  val name: String,
  val description: String,
  val category: String,
  val price: Int,
)

data class ProductResponse(
  val id: Long,
  val name: String,
  val description: String,
  val category: String,
  val price: Int,
) {
  companion object {
    fun from(product: Product) = ProductResponse(
      id = product.id!!,
      name = product.name,
      description = product.description,
      category = product.category,
      price = product.price,
    )
  }
}
