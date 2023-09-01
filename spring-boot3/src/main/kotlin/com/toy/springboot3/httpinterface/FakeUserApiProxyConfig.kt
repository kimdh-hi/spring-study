package com.toy.springboot3.httpinterface

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.support.WebClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import org.springframework.web.service.invoker.createClient

private const val FAKE_REST_API_URI = "https://fakerestapi.azurewebsites.net/api/v1/Users"

@Configuration
class FakeUserApiProxyConfig {

  @Bean
  fun fakeApi(): FakeUserApi {
    val webClient = WebClient.create(FAKE_REST_API_URI)

    val proxyFactory = HttpServiceProxyFactory
      .builder(WebClientAdapter.forClient(webClient))
      .build()

    return proxyFactory.createClient<FakeUserApi>()
  }

}