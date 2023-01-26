package com.toy.springwebfluxgraphql.lec14.exceptions

import org.springframework.graphql.execution.ErrorType

class BaseException(
  val errorType: ErrorType,
  message: String?,
  val extensions: Map<String, Any>
): RuntimeException(message)