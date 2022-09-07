package com.toy.userservice.domain

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class UserTransaction(
  @Id
  var id: Int? = null,
  val userId: Int,
  val amount: Int,
  val createdDate: LocalDateTime
)