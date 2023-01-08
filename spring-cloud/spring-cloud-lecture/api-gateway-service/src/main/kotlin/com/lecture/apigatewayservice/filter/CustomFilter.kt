package com.lecture.apigatewayservice.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class CustomFilter: AbstractGatewayFilterFactory<CustomFilter.Config>(Config::class.java) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun apply(config: Config): GatewayFilter = GatewayFilter { exchange, chain ->
    val request = exchange.request
    val response = exchange.response

    log.info("[CustomFilter] request.id: {}", request.id)

    chain.filter(exchange)
      .then(Mono.fromRunnable { log.info("[CustomFilter] response code: {}", response.statusCode) })
  }
  class Config
}