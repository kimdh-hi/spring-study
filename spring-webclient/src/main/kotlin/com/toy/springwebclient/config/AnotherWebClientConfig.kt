package com.toy.springwebclient.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AnotherWebClientConfig(
  private val webClientBuilder: WebClient.Builder
) {

  @Bean("anotherWebClient")
  fun anotherWebClient(): WebClient = webClientBuilder
    .baseUrl("https://fakerestapi.azurewebsites.net")
    .build()
}