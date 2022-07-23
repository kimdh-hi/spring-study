package com.toy.webfluxr2dbcpostgres.auth

import com.toy.webfluxr2dbcpostgres.common.Constants
import com.toy.webfluxr2dbcpostgres.common.UrlConstants
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

    val token = getToken(exchange)

    if (token.isNullOrBlank() || isNotSupportedTokenType(token) || checkSkipUrl(exchange))
      return chain.filter(exchange)

    val jwtPrincipal = jwtUtil.parse(token)
    val authentication = UsernamePasswordAuthenticationToken(jwtPrincipal, null, AuthorityUtils.NO_AUTHORITIES)

    return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withAuthentication(authentication))
  }

  private fun getToken(exchange: ServerWebExchange)
    = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.firstOrNull()

  private fun checkSkipUrl(exchange: ServerWebExchange): Boolean {
    return UrlConstants.JWT_FILTER_SKIP_URL.contains(exchange.request.path.value())
  }

  private fun isNotSupportedTokenType(token: String): Boolean {
    Constants.TOKEN_TYPE.forEach {
      if (token.substring(it.length) == it) return false
    }
    return true
  }
}