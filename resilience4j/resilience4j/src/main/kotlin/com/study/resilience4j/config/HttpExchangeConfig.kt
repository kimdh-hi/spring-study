package com.study.resilience4j.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.study.resilience4j.controller.ErrorResponse
import com.study.resilience4j.exception.BaseException
import com.study.resilience4j.httpclient.Test1Exchange
import com.study.resilience4j.httpclient.Test2Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory
import java.net.URI

@Configuration
class HttpExchangeConfig(
  private val objectMapper: ObjectMapper,
) {

  //for test
  @Bean
  fun test1Exchanger(): Test1Exchange {
    val restClient = RestClient.builder()
      .defaultStatusHandler(ExchangeErrorHandler(objectMapper))
      .baseUrl("http://localhost:8084")
      .build()

    val adapter = RestClientAdapter.create(restClient)

    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(Test1Exchange::class.java)
  }

  @Bean
  fun test2Client(): Test2Client {
    val restClient = RestClient.builder()
      .defaultStatusHandler(ExchangeErrorHandler(objectMapper))
      .baseUrl("http://localhost:8084")
      .build()

    val adapter = RestClientAdapter.create(restClient)
    val factory = HttpServiceProxyFactory.builderFor(adapter).build()

    return factory.createClient(Test2Client::class.java)
  }
}

private class ExchangeErrorHandler(
  private val objectMapper: ObjectMapper,
) : ResponseErrorHandler {
  override fun hasError(response: ClientHttpResponse): Boolean {
    return response.statusCode.isError
  }

  override fun handleError(url: URI, method: HttpMethod, response: ClientHttpResponse) {
    throw runCatching {
      val errorResponse = objectMapper.readValue<ErrorResponse>(response.body)
      BaseException(errorResponse.code, errorResponse.message)
    }.getOrElse { BaseException("9999", "error response parsing failed") }
  }
}

