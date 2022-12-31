package com.toy.springwebfluxgraphql.sec01.lec04.domain

data class Customer(
  val id: Long,
  val name: String,
  val age: Int,
  val address: String
)