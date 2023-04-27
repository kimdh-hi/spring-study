package com.lecture.userservice.service

import com.lecture.userservice.dto.TransactionRequestDto
import com.lecture.userservice.dto.TransactionResponseDto
import com.lecture.userservice.entity.TransactionStatus
import com.lecture.userservice.repository.UserRepository
import com.lecture.userservice.repository.UserTransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TransactionService(
  private val userRepository: UserRepository,
  private val userTransactionRepository: UserTransactionRepository
) {

  fun save(dto: TransactionRequestDto): Mono<TransactionResponseDto> {
    return userRepository.updateUserBalance(dto.userId, dto.amount)
      .filter { it }
      .map { dto.toEntity() }
      .flatMap { userTransactionRepository.save(it) }
      .map { TransactionResponseDto.of(it, TransactionStatus.APPROVED) }
      .defaultIfEmpty(TransactionResponseDto.of(dto, TransactionStatus.DECLINED))
  }
}