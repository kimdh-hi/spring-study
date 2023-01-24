package com.toy.springwebfluxgraphql.lec13.vo

data class CustomerEvent(
  val id: Int,
  val action: Action
)