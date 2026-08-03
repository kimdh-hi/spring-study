package com.study.springelasticsearch.product.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
  @Column(nullable = false, length = 200)
  var name: String,

  @Column(nullable = false, length = 1000)
  var description: String,

  @Column(nullable = false, length = 50)
  var category: String,

  @Column(nullable = false)
  var price: Int,

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  var id: Long? = null,
)
