package com.study.resilience4j.config

import org.springframework.web.client.HttpServerErrorException
import java.util.function.Predicate

class RestClientCircuitRecordFailurePredicate : Predicate<Throwable> {
  override fun test(t: Throwable): Boolean {
    return when (t) {
      is HttpServerErrorException -> t.statusCode.value() >= 500
      else -> false
    }
  }
}
