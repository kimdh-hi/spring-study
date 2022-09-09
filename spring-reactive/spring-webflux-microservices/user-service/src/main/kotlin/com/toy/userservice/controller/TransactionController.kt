package com.toy.userservice.controller

import com.toy.userservice.dto.TransactionRequestDto
import com.toy.userservice.dto.TransactionResponseDto
import com.toy.userservice.service.TransactionService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/users/transaction")
class TransactionController(
  private val transactionService: TransactionService
) {

  @PostMapping
  fun save(@RequestBody requestDto: Mono<TransactionRequestDto>): Mono<ResponseEntity<TransactionResponseDto>> {
    return requestDto
      .flatMap { transactionService.save(it) }
      .map { ResponseEntity.ok(it) }
  }
}