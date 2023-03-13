package com.toy.springhttpinterface.config

import com.toy.springhttpinterface.httpinterface.FakeApiCaller
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HttpInterfaceConfig {

  @Bean
  fun fakeApiCaller(webClient: WebClient) = HttpServiceProxyFactory
      .builder(WebClientAdapter.forClient(webClient))
      .build()
      .createClient(FakeApiCaller::class.java)
}