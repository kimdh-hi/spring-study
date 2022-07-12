package com.toy.reactivejdsl.security

import com.toy.reactivejdsl.common.UrlConstants
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

  override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {

    val token = getToken(exchange)

    if (token.isNullOrBlank() || checkSkipUrl(exchange))
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

}