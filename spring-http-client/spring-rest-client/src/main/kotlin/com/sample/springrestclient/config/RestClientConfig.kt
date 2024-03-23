package com.sample.springrestclient.config

import org.slf4j.LoggerFactory
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatusCode
import org.springframework.http.client.ClientHttpRequestExecution
import org.springframework.http.client.ClientHttpRequestInterceptor
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestClient
import org.springframework.web.client.RestClient.ResponseSpec.ErrorHandler

@Configuration
class RestClientConfig {

  @Bean
  fun restClient() = RestClient.builder()
    .defaultStatusHandler(RestClientErrorHandler())
    .requestInterceptor(RestClientLoggingInterceptor())
    .build()
}

class RestClientLoggingInterceptor: ClientHttpRequestInterceptor {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun intercept(
    request: HttpRequest,
    body: ByteArray,
    execution: ClientHttpRequestExecution
  ): ClientHttpResponse {
    log.info("RestClient request: ${request.method} ${request.uri}")
    val response = execution.execute(request, body)
    log.info("RestClient response: statusCode = ${response.statusCode}")

    return response
  }
}


class RestClientErrorHandler: ResponseErrorHandler {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun hasError(response: ClientHttpResponse): Boolean {
    return response.statusCode.is4xxClientError || response.statusCode.is5xxServerError
  }

  override fun handleError(response: ClientHttpResponse) {
    if (response.statusCode.is4xxClientError) {
      log.error("[RestClientErrorHandler] client error code={}, error={}", response.statusCode.value(), response.statusText)
    } else if(response.statusCode.is5xxServerError) {
      log.error("[RestClientErrorHandler] server error code={}, error={}", response.statusCode.value(), response.statusText)
    }
  }
}