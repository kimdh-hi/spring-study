package com.toy.springwebfluxgraphql.lec06.domain

import java.util.*

data class CustomerOrder(
  val id: UUID,
  val description: String
)