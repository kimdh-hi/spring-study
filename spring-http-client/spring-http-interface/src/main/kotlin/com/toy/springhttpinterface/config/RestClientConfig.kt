package com.toy.springhttpinterface.config

import org.springframework.boot.web.client.RestClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.web.client.ResponseErrorHandler
import org.springframework.web.client.RestClient
import java.net.URI

@Configuration
class RestClientConfig {

  @Bean
  fun restClient(restClientBuilder: RestClient.Builder): RestClient {
    return restClientBuilder.build()
  }

  @Bean
  fun restClientCustomizer() = RestClientCustomizer { customizer ->
    customizer.defaultStatusHandler(RestClientErrorHandler())
  }
}

private class RestClientErrorHandler : ResponseErrorHandler {
  override fun hasError(response: ClientHttpResponse): Boolean {
    return response.statusCode.isError
  }

  override fun handleError(url: URI, method: HttpMethod, response: ClientHttpResponse) {
    throw RuntimeException("$method $url, statusCode:${response.statusCode} message: ${response.statusText}")
  }
}
