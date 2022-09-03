package com.toy.springwebfluxbasic.config

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.function.BiFunction

@Service
class CalculatorHandler {

  fun additionalHandler(request: ServerRequest): Mono<ServerResponse> {
    return process(request) {a, b  -> ServerResponse.ok().bodyValue(a + b)}
  }

  fun subtractionHandler(request: ServerRequest): Mono<ServerResponse> {
    return process(request) {a, b  -> ServerResponse.ok().bodyValue(a - b)}
  }

  fun multiplicationHandler(request: ServerRequest): Mono<ServerResponse> {
    return process(request) {a, b  -> ServerResponse.ok().bodyValue(a * b)}
  }

  fun divisionHandler(request: ServerRequest): Mono<ServerResponse> {
    return process(request) { a, b ->
      if (b == 0)
        ServerResponse.badRequest().bodyValue("can not divide by 0")
      else
        ServerResponse.ok().bodyValue(a / b)
    }
  }

  private fun process(
    request: ServerRequest, operation: BiFunction<Int, Int, Mono<ServerResponse>>
  ): Mono<ServerResponse> {
    val a = getValue(request, "a")
    val b = getValue(request, "b")
    return operation.apply(a, b)
  }

  private fun getValue(request: ServerRequest, key: String): Int {
    return request.pathVariable(key).toInt()
  }
}