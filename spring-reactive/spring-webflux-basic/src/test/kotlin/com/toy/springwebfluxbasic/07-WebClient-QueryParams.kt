package com.toy.springwebfluxbasic

import com.toy.springwebfluxbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

class `07-WebClient-QueryParams`(val webClient: WebClient): BaseTest() {

  @Test
  fun `queryParams`() {
    // 1. UriComponentsBuilder 사용
//    val uriFormat = "http://localhost:8080/api/params?page={page}&size={size}"
//    val uri = UriComponentsBuilder.fromUriString(uriFormat)
//      .build(1, 10)

    // 3. Map 사용
    val params = LinkedMultiValueMap<String, String>()
    params.add("page", "1")
    params.add("size", "10")

    val responseFlux = webClient.get()
      .uri {
        // 2. path + query
//        it.path("/api/params").query("page={page}&size={size}").build(1, 10)
        it.path("/api/params").queryParams(params).build()
      }
      .retrieve()
      .bodyToFlux(Int::class.java)
      .doOnNext { println(it) }

    StepVerifier.create(responseFlux)
      .expectNextCount(2)
      .verifyComplete()
  }
}