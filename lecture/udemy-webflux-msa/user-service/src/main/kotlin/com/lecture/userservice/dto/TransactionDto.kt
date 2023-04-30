package com.lecture.userservice.dto

import com.lecture.userservice.entity.TransactionStatus
import com.lecture.userservice.entity.UserTransaction
import java.time.LocalDateTime

data class TransactionRequestDto(
  val userId: Int,
  val amount: Int
) {
  fun toEntity() = UserTransaction(userId = userId, amount = amount)
}

data class TransactionResponseDto(
  val userId: Int,
  val amount: Int,
  val status: TransactionStatus
) {
  companion object {
    fun of(transaction: UserTransaction, status: TransactionStatus) = TransactionResponseDto(
      userId = transaction.userId,
      amount = transaction.amount,
      status = status
    )

    fun of(dto: TransactionRequestDto, status: TransactionStatus) = TransactionResponseDto(
      userId = dto.userId,
      amount = dto.amount,
      status = status
    )
  }
}