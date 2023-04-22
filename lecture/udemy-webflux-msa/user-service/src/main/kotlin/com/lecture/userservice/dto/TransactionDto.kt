package com.lecture.userservice.dto

import com.lecture.userservice.entity.TransactionStatus

data class TransactionRequestDto(
  val userId: Int,
  val amount: Int
)

data class TransactionResponseDto(
  val userId: Int,
  val amount: Int,
  val status: TransactionStatus
)