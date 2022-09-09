package com.toy.orderservice.dto

import java.time.LocalDateTime

data class TransactionRequestDto(
  val userId: Int,
  val amount: Int
)

data class TransactionResponseDto(
  val userId: Int,
  val amount: Int,
  val status: TransactionStatus? = null,
  val createdDate: LocalDateTime? = null
)

enum class TransactionStatus {
  APPROVED, DECLINED
}