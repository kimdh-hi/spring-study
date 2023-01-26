package com.toy.springwebfluxgraphql.lec14.exceptions

import graphql.GraphQLError
import graphql.GraphqlErrorBuilder
import graphql.schema.DataFetchingEnvironment
import org.springframework.graphql.execution.DataFetcherExceptionResolver
import org.springframework.graphql.execution.ErrorType
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.Collections

@Component
class ExceptionResolver: DataFetcherExceptionResolver {

  override fun resolveException(
    throwable: Throwable, environment: DataFetchingEnvironment
  ): Mono<MutableList<GraphQLError>> {
    val exception = convert(throwable)
    return Mono.just(
      mutableListOf(
        GraphqlErrorBuilder.newError(environment)
          .message(exception.message)
          .errorType(exception.errorType)
          .extensions(exception.extensions)
          .build()
      )
    )
  }

  private fun convert(throwable: Throwable): BaseException {
    return if(BaseException::class == throwable::class) {
      throwable as BaseException
    } else {
      BaseException(ErrorType.INTERNAL_ERROR, throwable.message, Collections.emptyMap())
    }
  }
}