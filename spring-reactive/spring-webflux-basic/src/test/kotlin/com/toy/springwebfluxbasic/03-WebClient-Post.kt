package com.toy.springwebfluxbasic

import com.toy.springwebfluxbasic.base.BaseTest
import com.toy.springwebfluxbasic.dto.MultiplyRequestDto
import com.toy.springwebfluxbasic.dto.Response
import org.junit.jupiter.api.Test
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class `03-WebClient-Post`(val webClient: WebClient): BaseTest() {

  @Test
  fun `postTest`() {
    val requestDto = MultiplyRequestDto(first = 20, second = 10)
    val responseMono = webClient.post()
      .uri("/reactive-math/multiply")
      .bodyValue(requestDto)
      .retrieve()
      .bodyToMono(Response::class.java)
      .doOnNext { println(it) }

    StepVerifier.create(responseMono)
      .expectNextCount(1)
      .verifyComplete()
  }


}