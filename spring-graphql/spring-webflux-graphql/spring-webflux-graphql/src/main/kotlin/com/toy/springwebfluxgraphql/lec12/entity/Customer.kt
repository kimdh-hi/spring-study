package com.toy.springwebfluxgraphql.lec12.entity

import org.springframework.data.annotation.Id

data class Customer(
  @Id
  val id: Int? = null,
  var name: String,
  var age: Int,
  var city: String
)