package com.lecture.apigatewayservice.filter

import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class LoggingFilter: AbstractGatewayFilterFactory<LoggingFilter.Config>(Config::class.java) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun apply(config: Config): GatewayFilter = OrderedGatewayFilter({ exchange, chain ->
    val request = exchange.request
    val response = exchange.response

    log.info("[Logging-filter] baseMessage: {}", config.baseMessage)
    if(config.preLogger) {
      log.info("[Logging-filter] start request.id: {}", request.id)
    }

    chain.filter(exchange).then(Mono.fromRunnable {
      if(config.postLogger) {
        log.info("[Logging-filter] end response code: {}", response.statusCode)
      }
    })
//  }, Ordered.HIGHEST_PRECEDENCE) // GlobalFilter (default-filters) 보다 높은 우선순위
  }, Ordered.LOWEST_PRECEDENCE)

  data class Config(
    val baseMessage: String,
    val preLogger: Boolean,
    val postLogger: Boolean
  )
}