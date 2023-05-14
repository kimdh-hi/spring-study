package com.lecture.client

import com.lecture.dto.TransactionRequestDto
import com.lecture.dto.TransactionResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Component
class UserClient {

  @Value("\${user.service-url}")
  lateinit var url: String

  private val webClient: WebClient by lazy {
    WebClient.builder()
      .baseUrl(url)
      .build()
  }

  fun authorizeTransaction(dto: TransactionRequestDto): Mono<TransactionResponseDto> {
    return webClient.post()
      .uri("/transactions")
      .bodyValue(dto)
      .retrieve()
      .bodyToMono<TransactionResponseDto>()
  }
}