package com.toy.springwebfluxgraphql.lec05.domain

import java.util.UUID

data class Account(
  val id: UUID,
  val amount: Int,
  val accountType: AccountType
)