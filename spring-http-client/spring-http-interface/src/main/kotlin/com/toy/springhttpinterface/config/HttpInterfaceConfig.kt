package com.toy.springhttpinterface.config

import com.toy.springhttpinterface.httpinterface.FakeApiClient
import com.toy.springhttpinterface.httpinterface.MyTestApiClient
import com.toy.springhttpinterface.httpinterface.PingPongDto
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.MethodParameter
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpRequestValues
import org.springframework.web.service.invoker.HttpServiceArgumentResolver
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class HttpInterfaceConfig {

  @Bean
  fun fakeApiCaller(): FakeApiClient {
    val restClient = RestClient.builder()
      .baseUrl("https://fakerestapi.azurewebsites.net/api/v1")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(FakeApiClient::class.java)
  }

  @Bean
  fun myTestApiClient(): MyTestApiClient {
    val restClient = RestClient.builder()
      .baseUrl("http://localhost:8084/test")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory
      .builderFor(adapter)
      .customArgumentResolver(PingPongQueryArgumentResolver())
      .build()

    return factory.createClient(MyTestApiClient::class.java)
  }
}

class PingPongQueryArgumentResolver : HttpServiceArgumentResolver {
  override fun resolve(
    argument: Any?,
    parameter: MethodParameter,
    requestValues: HttpRequestValues.Builder
  ): Boolean {
    if (parameter.getParameterType() == PingPongDto::class.java) {
      val dto = argument as PingPongDto
      requestValues.addRequestParameter("data", dto.data)
        .addRequestParameter("date", dto.date.toString())
      return true
    }
    return false
  }
}
