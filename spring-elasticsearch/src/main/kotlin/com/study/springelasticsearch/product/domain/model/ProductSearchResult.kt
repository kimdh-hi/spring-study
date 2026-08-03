package com.study.springelasticsearch.product.domain.model

data class ProductSearchResult(
  val id: Long,
  val name: String,
  val category: String,
  val price: Int,
  val score: Float,
  val highlights: List<String>,
)
