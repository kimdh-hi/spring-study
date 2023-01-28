package com.toy.springwebfluxgraphql.lec14.interceptor

import org.slf4j.LoggerFactory
import org.springframework.graphql.server.WebGraphQlInterceptor
import org.springframework.graphql.server.WebGraphQlRequest
import org.springframework.graphql.server.WebGraphQlResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class RequestInterceptor: WebGraphQlInterceptor {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun intercept(request: WebGraphQlRequest, chain: WebGraphQlInterceptor.Chain): Mono<WebGraphQlResponse> {
    request.headers["some-header"]?.let {
      request.configureExecutionInput { t, u -> u.graphQLContext(mapOf("some-header" to it)).build() }
    }
    return chain.next(request)
  }
}