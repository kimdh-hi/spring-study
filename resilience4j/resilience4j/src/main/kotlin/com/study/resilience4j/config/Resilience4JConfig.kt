package com.study.resilience4j.config

import com.study.resilience4j.exception.BaseException
import io.github.resilience4j.circuitbreaker.CircuitBreaker
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerEvent
import io.github.resilience4j.circuitbreaker.event.CircuitBreakerOnCallNotPermittedEvent
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer
import io.github.resilience4j.core.registry.EntryAddedEvent
import io.github.resilience4j.core.registry.EntryRemovedEvent
import io.github.resilience4j.core.registry.EntryReplacedEvent
import io.github.resilience4j.core.registry.RegistryEventConsumer
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.ResourceAccessException
import java.util.function.Predicate


@Configuration
class Resilience4JConfig {

  private val log = LoggerFactory.getLogger(javaClass)

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

  @Bean
  fun customRegistryEventConsumer(): RegistryEventConsumer<CircuitBreaker> {
    return object : RegistryEventConsumer<CircuitBreaker> {
      override fun onEntryAddedEvent(entryAddedEvent: EntryAddedEvent<CircuitBreaker>) {
        val eventPublisher = entryAddedEvent.addedEntry.eventPublisher

        eventPublisher.onCallNotPermitted {
          log.error("[CircuitBreakerEvent.callNotPermitted] circuitName={}", it.circuitBreakerName)
        }

        eventPublisher.onStateTransition {
          val stateTransition = it.stateTransition
          log.info(
            "[CircuitBreakerEvent.stateTransition] {} state {} -> {}",
            it.circuitBreakerName,
            stateTransition.fromState,
            stateTransition.toState
          )
        }
      }

      override fun onEntryRemovedEvent(entryRemoveEvent: EntryRemovedEvent<CircuitBreaker>) {}

      override fun onEntryReplacedEvent(entryReplacedEvent: EntryReplacedEvent<CircuitBreaker>) {}
    }
  }
}
