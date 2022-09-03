package com.toy.springwebfluxbasic

import com.toy.springwebfluxbasic.base.BaseTest
import com.toy.springwebfluxbasic.dto.Response
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class `02-WebClient-Flux`(val webClient: WebClient): BaseTest() {

  @Test
  fun `flux`() {
    val responseFlux = webClient.get()
      .uri("/reactive-math/table/{number}", 5)
      .retrieve()
      .bodyToFlux(Response::class.java)
      .doOnNext { println(it) }

    StepVerifier.create(responseFlux)
      .expectNextCount(10)
      .verifyComplete()
  }

  @Test
  fun `fluxStream`() {
    val responseFlux = webClient.get()
      .uri("/reactive-math/table/{number}/stream", 5)
      .retrieve()
      .bodyToFlux(Response::class.java)
      .doOnNext { println(it) }

    StepVerifier.create(responseFlux)
      .expectNextCount(10)
      .verifyComplete()
  }
}