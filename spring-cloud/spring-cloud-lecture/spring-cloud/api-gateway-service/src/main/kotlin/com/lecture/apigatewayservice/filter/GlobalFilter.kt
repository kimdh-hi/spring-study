package com.lecture.apigatewayservice.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class GlobalFilter: AbstractGatewayFilterFactory<GlobalFilter.Config>(Config::class.java) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun apply(config: Config): GatewayFilter = GatewayFilter { exchange, chain ->
    val request = exchange.request
    val response = exchange.response

    log.info("[Global-filter] baseMessage: {}", config.baseMessage)
    if(config.preLogger) {
      log.info("[Global-filter] start request.id: {}", request.id)
    }

    chain.filter(exchange).then(Mono.fromRunnable {
      if(config.postLogger) {
        log.info("[Global-filter] end response code: {}", response.statusCode)
      }
    })
  }

  data class Config(
    val baseMessage: String,
    val preLogger: Boolean,
    val postLogger: Boolean
  )
}