package com.lecture.apigatewayservice.filter

import io.jsonwebtoken.Jwts
import org.slf4j.LoggerFactory
import org.springframework.cloud.gateway.filter.GatewayFilter
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory
import org.springframework.core.env.Environment
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class AuthorizationHeaderFilter(
  private val environment: Environment
): AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config>(Config::class.java) {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun apply(config: Config): GatewayFilter {
    return GatewayFilter { exchange, chain ->
      val request = exchange.request

      if(!request.headers.containsKey(HttpHeaders.AUTHORIZATION)) {
        return@GatewayFilter onError(exchange, "jwt token not exists.", HttpStatus.UNAUTHORIZED)
      }

      val authorizationHeader = request.headers[HttpHeaders.AUTHORIZATION]!![0]
      val token = authorizationHeader.replace("Bearer", "")

      if(!isValidToken(token)) {
        return@GatewayFilter onError(exchange, "invalid jwt token.", HttpStatus.UNAUTHORIZED)
      }

      chain.filter(exchange)
    }
  }

  private fun onError(exchange: ServerWebExchange, error: String, httpStatus: HttpStatus): Mono<Void> {
    log.error(error)
    val response = exchange.response
    response.statusCode = httpStatus
    return response.setComplete()
  }

  private fun isValidToken(token: String): Boolean {

    var returnValue = true

    val subject = try {
      val secret = environment.getProperty("token.secret")
      log.info("[isValidToken] secret: {}", secret)
      Jwts.parser().setSigningKey(secret)
        .parseClaimsJws(token).body
        .subject
    } catch (ex: Exception) {
      returnValue = false
      null
    }

    if(subject.isNullOrEmpty())
      returnValue = false

    return returnValue
  }

  class Config
}