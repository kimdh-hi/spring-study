package com.toy.springwebfluxgraphql.lec07.domain

import java.util.*

data class CustomerOrder(
  val id: UUID,
  val description: String
)