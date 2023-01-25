package com.toy.springwebfluxgraphql.lec14.controller

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolver
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class ExceptionResolver: DataFetcherExceptionResolver {

  override fun resolveException(
    exception: Throwable, environment: DataFetchingEnvironment
  ): Mono<MutableList<GraphQLError>> {
    return Mono.just(
      mutableListOf(
        GraphqlErrorBuilder.newError(environment)
          .message(exception.message)
          .errorType(ErrorType.INTERNAL_ERROR)
          .build()
      )
    )
  }
}