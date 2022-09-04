package com.toy.springwebfluxbasic.base

import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.test.context.TestConstructor
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class BaseTest

@Configuration
class WebClientConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun webClient(): WebClient {
    return WebClient.builder()
      .baseUrl("http://localhost:8080")
      .filter { request, exchangeFunction ->  generateAuth(request, exchangeFunction)}
      .build()
  }

//  private fun generateBearerToken(request: ClientRequest, exchangeFunction: ExchangeFunction): Mono<ClientResponse> {
//    log.info("generate token ...")
//    // HttpHeader - Authorization 에 bearer 타입 토큰 추가
//    val clientRequest = ClientRequest.from(request)
//      .headers { it.setBearerAuth("jwt-format-token...") }
//      .build()
//
//    return exchangeFunction.exchange(clientRequest)
//  }

  private fun generateAuth(request: ClientRequest, exchangeFunction: ExchangeFunction): Mono<ClientResponse> {
    val clientRequest = request.attribute("auth")
      .map {
        when(it) {
          "api" -> generateApiToken(request)
          "basic" -> generateBasicAuth(request)
          "bearer" -> generateBearerToken(request)
          else -> throw RuntimeException("not supported authentication type...")
        }
      }
      .orElse(request)
    return exchangeFunction.exchange(clientRequest)
  }

  private fun generateBasicAuth(request: ClientRequest): ClientRequest {
    return ClientRequest.from(request)
      .headers { it.setBasicAuth("username", "password") }
      .build()
  }

  private fun generateApiToken(request: ClientRequest): ClientRequest {
    return ClientRequest.from(request)
      .headers { it.set("Authorization", "api-token...") }
      .build()
  }

  private fun generateBearerToken(request: ClientRequest): ClientRequest {
    return ClientRequest.from(request)
      .headers { it.setBearerAuth("bearer token...") }
      .build()
  }
}