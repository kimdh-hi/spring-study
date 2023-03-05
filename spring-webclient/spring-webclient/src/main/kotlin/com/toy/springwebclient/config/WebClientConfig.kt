package com.toy.springwebclient.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.LoggingCodecSupport
import org.springframework.web.reactive.function.client.*
import reactor.core.publisher.Mono
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import java.time.Duration

@Configuration
class WebClientConfig(
  private val webClientProperties: WebClientProperties
) {

  companion object {
    val MAX_IDLE_AND_LIFE_TIME: Duration = Duration.ofSeconds(18)
  }

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  @Primary
  fun webClient(): WebClient {
    return webClientBuilder().build()
  }

  @Bean("otherWebClient")
  fun otherWebClient(): WebClient {
    return webClientBuilder().build().mutate().defaultHeaders {
      it.contentType = MediaType.APPLICATION_FORM_URLENCODED
      it.accept = listOf(MediaType.APPLICATION_JSON)
    }
      .build()
  }

  @Bean
  fun webClientBuilder(): WebClient.Builder {
    return WebClient.builder()
      .clientConnector(
        ReactorClientHttpConnector(
          HttpClient
            .create(getConnectionProvider())
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, webClientProperties.connectTimeoutMillis)
            .responseTimeout(Duration.ofSeconds(webClientProperties.responseTimeoutSeconds))
            .doOnConnected {
              ReadTimeoutHandler(webClientProperties.readWriteTimeoutSeconds)
              WriteTimeoutHandler(webClientProperties.readWriteTimeoutSeconds)
            }
        )
      )
      .exchangeStrategies(getExchangeStrategies())
      .filters {
        ExchangeFilterFunction.ofResponseProcessor { clientResponse: ClientResponse -> Mono.just(clientResponse) }
        ExchangeFilterFunction.ofRequestProcessor { clientRequest: ClientRequest -> Mono.just(clientRequest) }
        it.add(logRequest())
        it.add(logResponse())
      }
      .defaultHeaders { httpHeaders: HttpHeaders ->
        this.getDefaultHeaders(
          httpHeaders
        )
      }
      .defaultCookies {
        it.add("testaaa", "testbbb")
      }
  }

  private fun getConnectionProvider(): ConnectionProvider {
    return ConnectionProvider.builder("testProvider")
      .maxIdleTime(MAX_IDLE_AND_LIFE_TIME)
      .maxLifeTime(MAX_IDLE_AND_LIFE_TIME)
      .pendingAcquireTimeout(Duration.ofSeconds(webClientProperties.responseTimeoutSeconds))
      .lifo()
      .build()
  }

  private fun getExchangeStrategies(): ExchangeStrategies {
    val exchangeStrategies = ExchangeStrategies.builder()
      .codecs { it.defaultCodecs().maxInMemorySize(webClientProperties.maxByteCount) }
      .build()
    exchangeStrategies
      .messageWriters().stream()
      .filter {
        LoggingCodecSupport::class.java.isInstance(it)
      }
      .forEach {
        (it as LoggingCodecSupport).isEnableLoggingRequestDetails = true
      }
    return exchangeStrategies
  }

  private fun getDefaultHeaders(httpHeaders: HttpHeaders) {
    httpHeaders.contentType = MediaType.APPLICATION_JSON
    httpHeaders.accept = listOf(MediaType.APPLICATION_JSON)
  }

  private fun logRequest() : ExchangeFilterFunction {
    return ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
      log.debug("webClient request: {} {}", clientRequest.method(), clientRequest.url())
      Mono.just(clientRequest)
    }
  }

  private fun logResponse() : ExchangeFilterFunction {
    return ExchangeFilterFunction.ofResponseProcessor { response ->
      log.debug("webClient Response: {}", response.statusCode())
      Mono.just(response)
    }
  }
}