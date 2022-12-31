package com.toy.springwebfluxgraphql.sec01.lec04.domain

import java.util.*

data class CustomerOrder(
  val id: UUID,
  val description: String
)