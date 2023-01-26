package com.toy.springwebfluxgraphql.lec14.exceptions

import com.toy.springwebfluxgraphql.lec14.vo.CustomerVO
import org.springframework.graphql.execution.ErrorType
import reactor.core.publisher.Mono

class Errors {

  companion object {
    fun <T> notFound(id: Int): Mono<T> = Mono.error(
      BaseException(ErrorType.BAD_REQUEST, "not found...", mapOf("id" to id))
    )

    fun <T> ageShouldBeLargerThan19(customerVO: CustomerVO): Mono<T> = Mono.error(
      BaseException(ErrorType.BAD_REQUEST, "age should be larger than 19", mapOf("customer" to customerVO))
    )
  }
}