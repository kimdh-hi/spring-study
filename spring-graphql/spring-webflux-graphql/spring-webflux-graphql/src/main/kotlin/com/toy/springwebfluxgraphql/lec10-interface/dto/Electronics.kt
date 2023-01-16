package com.toy.springwebfluxgraphql.`lec10-interface`.dto

import java.time.LocalDateTime
import java.util.*

data class Electronics(
  val id: UUID = UUID.randomUUID(),
  val description: String,
  val price: Int,
  val brand: String
)