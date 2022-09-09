package com.toy.userservice.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("tb_user_transaction")
data class UserTransaction(
  @Id
  var id: Int? = null,
  val userId: Int,
  val amount: Int,
  val createdDate: LocalDateTime
)