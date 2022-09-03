package com.toy.springwebfluxbasic.base

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestConstructor
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BaseTest

@Configuration
class WebClientConfig {

  @Bean
  fun webClient(): WebClient {
    return WebClient.builder()
      .baseUrl("http://localhost:8080")
      .build()
  }
}