package com.toy.springhttpinterface.config

import com.toy.springhttpinterface.httpinterface.FakeApiCaller
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HttpInterfaceConfig {

  @Bean
  fun fakeApiCaller(): FakeApiCaller {
    val restClient = RestClient.builder()
      .baseUrl("https://fakerestapi.azurewebsites.net/api/v1")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(FakeApiCaller::class.java)
  }
}
