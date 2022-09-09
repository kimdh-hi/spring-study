package com.toy.orderservice.client

import com.toy.orderservice.dto.TransactionRequestDto
import com.toy.orderservice.dto.TransactionResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class UserClient(
  @Value("\${user-service.url}") private val url: String
) {

  private val webClient = WebClient.builder()
    .baseUrl(url)
    .build()

  fun transactionRequest(requestDto: TransactionRequestDto): Mono<TransactionResponseDto> {
    return webClient.post()
      .uri(url)
      .bodyValue(requestDto)
      .retrieve()
      .bodyToMono(TransactionResponseDto::class.java)
  }
}