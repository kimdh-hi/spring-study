package com.toy.springwebfluxgraphql.`lec10-interface`.dto

import java.time.LocalDateTime
import java.util.*

data class FruitDto(
  val id: UUID = UUID.randomUUID(),
  val description: String,
  val price: Int,
  val expireDate: LocalDateTime
)