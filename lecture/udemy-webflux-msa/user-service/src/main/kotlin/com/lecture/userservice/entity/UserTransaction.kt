package com.lecture.userservice.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("user_transaction ")
class UserTransaction(
  @Id
  var id: Int? = null,
  var userId: Int,
  val amount: Int,
  val transactionDate: LocalDateTime
)