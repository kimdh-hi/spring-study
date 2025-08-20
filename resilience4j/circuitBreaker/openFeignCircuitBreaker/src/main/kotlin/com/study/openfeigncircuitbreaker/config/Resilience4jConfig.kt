package com.study.openfeigncircuitbreaker.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.cloud.openfeign.CircuitBreakerNameResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class Resilience4jConfig {
  @Bean
  fun defaultCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {
    val customConfig = CircuitBreakerConfig.custom()
      .slidingWindow(5, 5, CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
      .failureRateThreshold(100F)
      .waitDurationInOpenState(Duration.ofSeconds(10))
      .permittedNumberOfCallsInHalfOpenState(10)
      .build()

    return Customizer { factory ->
      factory.configureDefault { id ->
        Resilience4JConfigBuilder(id)
          .circuitBreakerConfig(customConfig)
//          .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
          .build()
      }
    }
  }

  @Bean
  fun circuitBreakerNameResolver(): CircuitBreakerNameResolver {
    return CircuitBreakerNameResolver { feignClientName, target, method ->
      feignClientName
    }
  }
}
