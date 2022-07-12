package com.toy.reactivejdsl.security

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class JwtServerAuthenticationConverter: ServerAuthenticationConverter {

  override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
    return Mono.justOrEmpty(exchange)
      .flatMap { Mono.justOrEmpty(it.request.headers[HttpHeaders.AUTHORIZATION]) }
      .filter { it.isNotEmpty() }
      .map { it.first() }
      .map { UsernamePasswordAuthenticationToken(it, null, AuthorityUtils.NO_AUTHORITIES) }

  }
}