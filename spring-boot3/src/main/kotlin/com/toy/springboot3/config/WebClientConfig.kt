package com.toy.springboot3.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

  @Bean
  fun webClient(webClientBuilder: WebClient.Builder) = webClientBuilder.build()

  @Bean(name = ["otherWebClient"])
  fun otherWebClient(webClientBuilder: WebClient.Builder) = webClientBuilder.defaultHeaders {
    it.contentType = MediaType.APPLICATION_FORM_URLENCODED
    it.accept = listOf(MediaType.APPLICATION_JSON)
  }
    .build()
}
