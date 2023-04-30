package com.lecture.userservice.controller

import com.lecture.userservice.dto.TransactionRequestDto
import com.lecture.userservice.dto.TransactionResponseDto
import com.lecture.userservice.service.TransactionService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/transactions")
class TransactionController(
  private val transactionService: TransactionService
) {

  @PostMapping
  fun save(@RequestBody dto: Mono<TransactionRequestDto>): Mono<TransactionResponseDto> {
    return dto.flatMap { transactionService.save(it) }
  }
}