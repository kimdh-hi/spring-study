package com.toy.springwebfluxgraphql.lec14.exceptions

import org.springframework.graphql.execution.ErrorType
import reactor.core.publisher.Mono

class Errors {

  companion object {
    fun <T> notFound(id: Int): Mono<T> = Mono.error(
      BaseException(ErrorType.BAD_REQUEST, "not found...", mapOf("id" to id))
    )
  }
}