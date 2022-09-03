package com.toy.springwebfluxbasic.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.*

@Configuration
class CalculatorRouterConfig(
  private val handler: CalculatorHandler
) {

  @Bean
  fun calculatorPrefixRouter(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      .path("/calculator", this::calculatorRouter)
      .build()
  }

  private fun calculatorRouter(): RouterFunction<ServerResponse> {
    return RouterFunctions.route()
      .GET("/{a}/{b}", checkOperationFromHeader("+"), handler::additionalHandler)
      .GET("/{a}/{b}", checkOperationFromHeader("-"), handler::subtractionHandler)
      .GET("/{a}/{b}", checkOperationFromHeader("*"), handler::multiplicationHandler)
      .GET("/{a}/{b}", checkOperationFromHeader("/"), handler::divisionHandler)
      .GET("/{a}/{b}") { ServerResponse.badRequest().bodyValue("error... should be contains operation in headers") }
      .build()
  }

  private fun checkOperationFromHeader(operation: String): RequestPredicate {
    return RequestPredicates.headers {
      operation == getOperationFromHeader("OP", it.asHttpHeaders().toSingleValueMap())
    }
  }

  private fun getOperationFromHeader(operationHeaderPrefix: String, headers: Map<String, String>): String {
    return headers[operationHeaderPrefix] ?: throw RuntimeException("operation not exists ...")
  }
}