package com.toy.coroutinevsreactive.coroutine.controller

import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest
@AutoConfigureWebTestClient
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
internal class CoroutineUserControllerTest(val webClient: WebTestClient) {

  @Test
  fun test() {
    webClient.get()
      .uri {
        it.path("/api/coroutine/users")
        it.queryParam("name", "kim")
          .build()
      }.exchange()
  }
}