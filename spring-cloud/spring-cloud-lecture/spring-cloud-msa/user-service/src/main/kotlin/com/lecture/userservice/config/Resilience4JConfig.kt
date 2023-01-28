package com.lecture.userservice.config

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig
import io.github.resilience4j.timelimiter.TimeLimiterConfig
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder
import org.springframework.cloud.client.circuitbreaker.Customizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Duration

@Configuration
class Resilience4JConfig {

  @Bean
  fun resilience4jCustomizer(): Customizer<Resilience4JCircuitBreakerFactory> {

    val circuitBreakerConfig = CircuitBreakerConfig.custom()
      .failureRateThreshold(4f)
      .waitDurationInOpenState(Duration.ofSeconds(1))
      .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
      .slidingWindowSize(2)
      .build()

    val timeLimiterConfig = TimeLimiterConfig.custom()
      .timeoutDuration(Duration.ofSeconds(3))
      .build()

    return Customizer { factory ->
      factory.configureDefault { id ->
        Resilience4JConfigBuilder(id)
          .timeLimiterConfig(timeLimiterConfig)
          .circuitBreakerConfig(circuitBreakerConfig)
          .build()
      }
    }
  }
}