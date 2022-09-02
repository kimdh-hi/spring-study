package com.toy.springwebfluxbasic.config

import com.toy.springwebfluxbasic.dto.MultiplyRequestDto
import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.service.ReactiveMathService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono

@Configuration
class RouterConfig(
  private val requestHandler: RequestHandler
) {

  @Bean
  fun serverFunctionalRouterFunction(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      .GET("/router/square/{input}", requestHandler::squareHandler)
      .GET("/router/table/{input}", requestHandler::tableHandler)
      .GET("/router/table/{input}/stream", requestHandler::tableStreamHandler)
      .POST("/router/multiply", requestHandler::multiplyHandler)
      .build()
  }
}

@Service
class RequestHandler(
  private val mathService: ReactiveMathService
) {

  fun squareHandler(serverRequest: ServerRequest): Mono<ServerResponse> {
    val input: Int = serverRequest.pathVariable("input").toInt()
    val responseMono = mathService.getSquare(input)
    return ServerResponse.ok().body(responseMono, Response::class.java)
  }

  fun tableHandler(serverRequest: ServerRequest): Mono<ServerResponse> {
    val input = serverRequest.pathVariable("input").toInt()
    val responseFlux = mathService.multiplicationTable(input)
    return ServerResponse.ok().body(responseFlux, Response::class.java)
  }

  fun tableStreamHandler(serverRequest: ServerRequest): Mono<ServerResponse> {
    val input = serverRequest.pathVariable("input").toInt()
    val responseFlux = mathService.multiplicationTable(input)
    return ServerResponse.ok()
      .contentType(MediaType.TEXT_EVENT_STREAM)
      .body(responseFlux, Response::class.java)
  }

  fun multiplyHandler(serverRequest: ServerRequest): Mono<ServerResponse> {
    val requestMono = serverRequest.bodyToMono(MultiplyRequestDto::class.java)
    val responseMono = mathService.multiply(requestMono)
    return ServerResponse.ok()
      .body(responseMono, Response::class.java)
  }
}