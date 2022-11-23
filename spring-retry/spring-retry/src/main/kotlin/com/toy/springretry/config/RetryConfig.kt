package com.toy.springretry.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.RetryCallback
import org.springframework.retry.RetryContext
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.listener.RetryListenerSupport
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import java.util.*

@Configuration
class RetryConfig {

  @Bean
  fun retryTemplate(): RetryTemplate {
    val retryTemplate = RetryTemplate().apply {
      registerListener(CustomRetryListener())
    }

    val backOffPolicy = FixedBackOffPolicy().apply {
      backOffPeriod = 2000L
    }
    retryTemplate.setBackOffPolicy(backOffPolicy)

    val defaultMaxAttempts = 5
    val retryPolicy = SimpleRetryPolicy(defaultMaxAttempts, Collections.singletonMap(Exception::class.java, true))

    retryTemplate.setRetryPolicy(retryPolicy)
    return retryTemplate
  }
}

class CustomRetryListener: RetryListenerSupport() {

  private val log = LoggerFactory.getLogger(javaClass)

  override fun <T : Any?, E : Throwable?> open(context: RetryContext?, callback: RetryCallback<T, E>?): Boolean {
    log.debug("retry start")
    return super.open(context, callback)
  }

  override fun <T : Any?, E : Throwable?> close(
    context: RetryContext?,
    callback: RetryCallback<T, E>?,
    throwable: Throwable?
  ) {
    log.debug("retry close")
    super.close(context, callback, throwable)
  }

  override fun <T : Any?, E : Throwable?> onError(
    context: RetryContext,
    callback: RetryCallback<T, E>,
    throwable: Throwable
  ) {
    log.debug("retry onError: {}", context.lastThrowable.stackTrace[0])
    super.onError(context, callback, throwable)
  }
}