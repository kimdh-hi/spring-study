package com.lecture.dto

data class TransactionRequestDto(
  val userId: Int,
  val amount: Int
)

data class TransactionResponseDto(
  val userId: Int,
  val amount: Int,
  val status: TransactionStatus? = null
)

enum class TransactionStatus {
  APPROVED,
  DECLINED,
}