package com.toy.springwebfluxbasic.`01-get-single-response`

import com.toy.springwebfluxbasic.base.BaseTest
import com.toy.springwebfluxbasic.dto.Response
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class WebClientTestBasic1(val webClient: WebClient): BaseTest() {

  @Test
  fun `block`() {
    val response = webClient
      .get()
      .uri("/reactive-math/square/{number}" , 15)
      .retrieve()
      .bodyToMono(Response::class.java)
      .block()

    println(response)
  }

  @Test
  fun `stepVerifier`() {
    val responseMono = webClient
      .get()
      .uri("/reactive-math/square/{number}" , 5)
      .retrieve()
      .bodyToMono(Response::class.java)

    StepVerifier.create(responseMono)
      .expectNextMatches { it.output == 25 }
      .verifyComplete()
  }
}