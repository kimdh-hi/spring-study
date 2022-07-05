package com.toy.webfluxr2dbcpostgres.auth

import com.toy.webfluxr2dbcpostgres.common.Constants
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class JwtAuthenticationFilter(
  private val jwtUtil: JwtUtil,
): WebFilter {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

    if (checkSkipUrl(exchange))
      return chain.filter(exchange)

    val token = getToken(exchange) ?: throw RuntimeException("token empty..")
    val jwtPrincipal = jwtUtil.parse(token)
    val authentication = UsernamePasswordAuthenticationToken(jwtPrincipal, null, AuthorityUtils.NO_AUTHORITIES)

    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
  }

  private fun getToken(exchange: ServerWebExchange)
    = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()

  private fun checkSkipUrl(exchange: ServerWebExchange): Boolean {
    log.info("request.uri: {}", exchange.request.path.value())
    return Constants.JWT_FILTER_SKIL_URL.contains(exchange.request.path.value())
  }
}