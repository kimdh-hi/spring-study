package com.study.springelasticsearch.product.domain.model

import jakarta.persistence.Column
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

  @Column(nullable = false, length = 200)
  var name: String,

  @Column(nullable = false, length = 1000)
  var description: String,

  @Column(nullable = false, length = 50)
  var category: String,

  @Column(nullable = false)
  var price: Int,
) {
  companion object {
    fun of(name: String, description: String, category: String, price: Int) = Product(
      name = name,
      description = description,
      category = category,
      price = price,
    )
  }
}
