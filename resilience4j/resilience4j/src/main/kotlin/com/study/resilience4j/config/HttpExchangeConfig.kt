package com.study.resilience4j.config

import com.study.resilience4j.httpclient.Test1Exchange
import com.study.resilience4j.httpclient.Test2Exchange
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HttpExchangeConfig {

  //for test
  @Bean
  fun test1Exchanger(): Test1Exchange {
    val restClient = RestClient.builder()
      .baseUrl("http://localhost:8084")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(Test1Exchange::class.java)
  }

  @Bean
  fun test2Exchanger(): Test2Exchange {
    val restClient = RestClient.builder()
      .baseUrl("http://localhost:8084")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(Test2Exchange::class.java)
  }
}
