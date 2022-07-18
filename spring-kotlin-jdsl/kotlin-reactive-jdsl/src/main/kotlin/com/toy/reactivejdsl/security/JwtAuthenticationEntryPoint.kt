package com.toy.reactivejdsl.security

import com.toy.reactivejdsl.common.ErrorCodes
import com.toy.reactivejdsl.common.toJson
import com.toy.reactivejdsl.vo.ErrorResponseVO
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

class JwtAuthenticationEntryPoint : ServerAuthenticationEntryPoint {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun commence(exchange: ServerWebExchange, ex: AuthenticationException?): Mono<Void> {
    log.error(ex?.message ?: "failed to authenticate" )
    val response = exchange.response
    val body = createErrorResponse(response)
    val buffer = response.bufferFactory().wrap(body.toByteArray(StandardCharsets.UTF_8))
    return response.writeWith(Mono.just(buffer))
  }

  private fun createErrorResponse(response: org.springframework.http.server.reactive.ServerHttpResponse): String {
    val errorResponseVO = ErrorResponseVO(ErrorCodes.UNAUTHORIZED_ACCESS, message = "failed to authenticate")
    response.statusCode = HttpStatus.UNAUTHORIZED
    response.headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
    return toJson(errorResponseVO)
  }
}