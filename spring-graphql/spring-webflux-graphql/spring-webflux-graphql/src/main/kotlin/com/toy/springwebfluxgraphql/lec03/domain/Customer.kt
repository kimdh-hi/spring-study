package com.toy.springwebfluxgraphql.lec03.domain

data class Customer(
  val id: Long,
  val name: String,
  val age: Int,
  val address: String
)