package com.toy.userservice.service

import com.toy.userservice.dto.TransactionRequestDto
import com.toy.userservice.dto.TransactionResponseDto
import com.toy.userservice.dto.TransactionStatus
import com.toy.userservice.repository.UserRepository
import com.toy.userservice.repository.UserTransactionRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class TransactionService(
  private val userTransactionRepository: UserTransactionRepository,
  private val userRepository: UserRepository
) {

  fun save(requestDto: TransactionRequestDto): Mono<TransactionResponseDto> =
    userRepository.updateUserBalance(requestDto.userId, requestDto.amount)
      .filter { it == false }
      .map { requestDto.toEntity() }
      .flatMap { userTransactionRepository.save(it) }
      .map { TransactionResponseDto.of(requestDto, TransactionStatus.APPROVED) }
      .defaultIfEmpty(TransactionResponseDto.of(requestDto, TransactionStatus.DECLINED))
}