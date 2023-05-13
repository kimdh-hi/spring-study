package com.lecture.client

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ProductClient {

  @Value("\${product.service-url}")
  lateinit var url: String

  private val webClient: WebClient by lazy {
    WebClient.builder()
      .baseUrl(url)
      .build()
  }

  fun getProductIdById(productId: String): Mono<ProductClient> {
    return webClient.get()
      .uri("/{id}", productId)
      .retrieve()
      .bodyToMono(ProductClient::class.java)
  }
}