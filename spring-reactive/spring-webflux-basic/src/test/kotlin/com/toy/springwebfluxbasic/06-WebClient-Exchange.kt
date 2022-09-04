package com.toy.springwebfluxbasic

import com.toy.springwebfluxbasic.base.BaseTest
import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.exception.InputValidationException
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class `06-WebClient-Exchange`(val webClient: WebClient): BaseTest()  {

  // exchange = retrieve + @ (httpStatus ...)
  @Test
  fun `exchange`() {
    val responseMono = webClient.get()
      .uri("/reactive-math/square/{number}/error/throw", 5)
      .exchangeToMono { exchange(it) }
      .doOnNext { println(it) }
      .doOnError { println(it.message) }

    StepVerifier.create(responseMono)
      .expectNextCount(1)
      .verifyComplete()
  }

  private fun exchange(clientResponse: ClientResponse): Mono<Any> {
    return if(clientResponse.rawStatusCode() == 400)
      clientResponse.bodyToMono(InputValidationException::class.java)
    else
      clientResponse.bodyToMono(Response::class.java)
  }
}