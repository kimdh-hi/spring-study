package com.toy.springwebfluxbasic.config

import com.toy.springwebfluxbasic.dto.ErrorResponse
import com.toy.springwebfluxbasic.dto.MultiplyRequestDto
import com.toy.springwebfluxbasic.dto.Response
import com.toy.springwebfluxbasic.exception.InputValidationException
import com.toy.springwebfluxbasic.service.ReactiveMathService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.function.BiFunction

@Configuration
class RouterConfig(
  private val requestHandler: RequestHandler
) {

  @Bean
  fun highLevelRouter(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      .path("/router", this::serverFunctionalRouterFunction)
      .build()
  }

  @Bean
  fun serverFunctionalRouterFunction(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      // RequestPredicates.path("*/1?") -> 경로변수 input 이 "1x" 혹은 20 인 경우에만 squareHandler 와 매칭시킨다. ex) 11, 12 ok  1, 21 no
      .GET("/square/{input}",
        RequestPredicates.path("*/1?").or(RequestPredicates.path("*/20")),
        requestHandler::squareHandler
      )
      // 위 패턴과 매칭되지 않은 모든 /router/square/{input} 요청은 아래 핸들러로 매칭된다.
      .GET("/square/{input}") {
        ServerResponse
          .badRequest()
          .bodyValue(ErrorResponse(errorCode = InputValidationException.ERROR_CODE, message = InputValidationException.MESSAGE))
      }
      .GET("/table/{input}", requestHandler::tableHandler)
      .GET("/table/{input}/stream", requestHandler::tableStreamHandler)
      .POST("/multiply", requestHandler::multiplyHandler)
      .GET("/square/{input}/error", requestHandler::squareHandlerWithValidation)
      .onError(InputValidationException::class.java, exceptionHandler())
      .build()
  }

  private fun exceptionHandler(): BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> {
    return BiFunction { ex, _ ->
      val inputValidationException = ex as InputValidationException
      val response = ErrorResponse(
        errorCode = inputValidationException.errorCode,
        message = inputValidationException.message!!
      )
      return@BiFunction ServerResponse.badRequest().bodyValue(response)
    }
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

  fun squareHandlerWithValidation(serverRequest: ServerRequest): Mono<ServerResponse> {
    val input = serverRequest.pathVariable("input").toInt()
    if(input > 20 || input < 10) {
//      val inputValidationException = InputValidationException()
//      return ServerResponse.badRequest().bodyValue(inputValidationException)
      return Mono.error(InputValidationException())
    }

    val responseMono = mathService.getSquare(input)
    return ServerResponse.ok().body(responseMono, Response::class.java)
  }
}