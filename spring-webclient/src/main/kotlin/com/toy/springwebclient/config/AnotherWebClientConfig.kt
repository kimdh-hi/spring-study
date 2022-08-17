package com.toy.springwebclient.config

import com.toy.springwebclient.common.WebClientConstants
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class AnotherWebClientConfig(
  private val webClientBuilder: WebClient.Builder
) {

  @Bean(WebClientConstants.ANOTHER)
  fun anotherWebClient(): WebClient = webClientBuilder
    .baseUrl("https://fakerestapi.azurewebsites.net")
    .defaultCookies {
      it.add("test111", "test222")
    }
    .build()
}