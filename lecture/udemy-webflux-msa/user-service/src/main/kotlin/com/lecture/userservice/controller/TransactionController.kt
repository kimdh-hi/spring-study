package com.lecture.userservice.controller

import com.lecture.userservice.dto.TransactionRequestDto
import com.lecture.userservice.dto.TransactionResponseDto
import com.lecture.userservice.service.TransactionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
class TransactionController(
  private val transactionService: TransactionService
) {

  @PostMapping("/users/transactions")
  fun save(@RequestBody dto: Mono<TransactionRequestDto>): Mono<TransactionResponseDto> {
    return dto.flatMap { transactionService.save(it) }
  }

  @GetMapping("/users/{userId}/transactions")
  fun getByUserId(@PathVariable userId: Int): Flux<TransactionResponseDto> {
    return transactionService.getByUserId(userId)
  }
}