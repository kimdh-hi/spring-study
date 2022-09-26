package com.lecture.userservice.config.annotation

import org.springframework.core.MethodParameter
import org.springframework.http.HttpHeaders
import org.springframework.web.reactive.BindingContext
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class AuthToken

class AuthTokenResolver: HandlerMethodArgumentResolver {
  override fun supportsParameter(parameter: MethodParameter): Boolean
    = parameter.hasParameterAnnotation(AuthToken::class.java)

  override fun resolveArgument(
    parameter: MethodParameter,
    bindingContext: BindingContext,
    exchange: ServerWebExchange
  ): Mono<Any> {
    val authHeader = exchange.request.headers[HttpHeaders.AUTHORIZATION]?.first()
    checkNotNull(authHeader)
    val token = if(authHeader.split(" ").size > 1) {
      authHeader.split(" ")[1]
    } else {
      authHeader
    }
    return token.toMono()
  }
}