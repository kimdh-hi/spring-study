package com.toy.springwebfluxbasic

import com.toy.springwebfluxbasic.base.BaseTest
import com.toy.springwebfluxbasic.dto.Response
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.test.StepVerifier

class `05-WebClient-Error`(val webClient: WebClient): BaseTest() {

  @Test
  fun badRequest() {
    val responseMono = webClient.get()
      .uri("/reactive-math/square/{number}/error/throw", 5)
      .retrieve()
      .bodyToMono(Response::class.java)
      .doOnNext { println(it) }
      .doOnError { println(it.message) }

    StepVerifier.create(responseMono)
      .verifyError(WebClientResponseException.BadRequest::class.java)
  }
}