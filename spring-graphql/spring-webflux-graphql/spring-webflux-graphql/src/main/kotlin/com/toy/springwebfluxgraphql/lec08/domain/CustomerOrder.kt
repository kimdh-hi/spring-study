package com.toy.springwebfluxgraphql.lec08.domain

import java.util.*

data class CustomerOrder(
  val id: UUID,
  val description: String
)