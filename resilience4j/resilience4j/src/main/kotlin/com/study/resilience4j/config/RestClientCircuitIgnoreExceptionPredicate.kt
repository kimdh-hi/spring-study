package com.study.resilience4j.config

import com.study.resilience4j.exception.BaseException
import java.util.function.Predicate

class RestClientCircuitIgnoreExceptionPredicate : Predicate<Throwable> {
  override fun test(throwable: Throwable): Boolean {
    return when (throwable) {
      is BaseException -> throwable.errorCode != "9999"
      else -> true
    }
  }
}
