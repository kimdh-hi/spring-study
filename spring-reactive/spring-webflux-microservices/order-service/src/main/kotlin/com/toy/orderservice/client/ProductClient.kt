package com.toy.orderservice.client

import com.toy.orderservice.dto.ProductDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ProductClient(
  @Value("\${product-service.url}") private val url: String
) {

  private val webClient = WebClient.builder()
    .baseUrl(url)
    .build()

  fun getProductById(productId: Int): Mono<ProductDto> {
    return webClient.get()
      .uri("/{id}", productId)
      .retrieve()
      .bodyToMono(ProductDto::class.java)
  }
}