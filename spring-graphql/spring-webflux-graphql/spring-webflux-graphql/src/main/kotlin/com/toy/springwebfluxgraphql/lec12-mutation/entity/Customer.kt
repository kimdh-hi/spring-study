package com.toy.springwebfluxgraphql.`lec12-mutation`.entity

data class Customer(
  val id: Int? = null,
  var name: String,
  var age: Int,
  var city: String
)