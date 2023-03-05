package com.toy.springwebclient.config

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestConstructor
import org.springframework.web.reactive.function.client.WebClient

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class WebClientConfigTest(
  private val webClient: WebClient,
  @Qualifier("otherWebClient") private val otherWebClient: WebClient
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun test() {
    log.info("webclient: {}", webClient)
    log.info("otherWebClient: {}", otherWebClient)
  }
}