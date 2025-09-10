package com.study.resilience4j.config

import com.study.resilience4j.exception.BaseException
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.ResourceAccessException
import java.util.function.Predicate


@Configuration
class Resilience4JConfig {

  @Bean
  fun defaultCircuitBreakerConfigCustomizer(): CircuitBreakerConfigCustomizer {
    return CircuitBreakerConfigCustomizer.of("default") {
      it.ignoreException(restClientCircuitIgnoreExceptionPredicate())
    }
  }

  private fun restClientCircuitIgnoreExceptionPredicate(): Predicate<Throwable> {
    return Predicate<Throwable> { throwable ->
      when (throwable) {
        is BaseException -> throwable.errorCode != "9999"
        is ResourceAccessException -> false
        else -> true
      }
    }
  }
}
