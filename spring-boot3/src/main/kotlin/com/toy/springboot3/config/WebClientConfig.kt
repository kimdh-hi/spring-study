package com.toy.springboot3.config

import org.slf4j.LoggerFactory
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ClientRequest
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class WebClientConfig {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun webClient(webClientBuilder: WebClient.Builder) = webClientBuilder.build()

  @Bean(name = ["otherWebClient"])
  fun otherWebClient(webClientBuilder: WebClient.Builder) = webClientBuilder.defaultHeaders {
    it.contentType = MediaType.APPLICATION_FORM_URLENCODED
    it.accept = listOf(MediaType.APPLICATION_JSON)
  }
    .build()

  @Bean
  fun webClientCustomizer() = WebClientCustomizer { customizer ->
    customizer.filters { filter ->
      ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse -> Mono.just(clientResponse) }
      ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest -> Mono.just(clientRequest) }
      filter.add(logRequest())
      filter.add(logResponse())
    }
  }

  private fun logRequest() : ExchangeFilterFunction {
    return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
      log.info("webClient Request: {} {}", clientRequest.method(), clientRequest.url())
      Mono.just(clientRequest)
    }
  }

  private fun logResponse() : ExchangeFilterFunction {
    return ExchangeFilterFunction.ofResponseProcessor { response ->
      log.info("webClient Response: {}", response.statusCode())
      Mono.just(response)
    }
  }
}