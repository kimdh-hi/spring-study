package com.toy.springretry.utils

import com.toy.springretry.config.CustomRetryListener
import org.springframework.retry.backoff.FixedBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component

@Component
class RetryUtils(
  private val retryTemplate: RetryTemplate
) {

  @Throws(Exception::class)
  fun <T> run(
    maxAttempts: Int,
    intervalMs: Long,
    exceptions: List<Class<out Throwable>> = listOf(Exception::class.java),
    task: () -> T
  ): T {
    retryTemplate.registerListener(CustomRetryListener())

    val backOffPolicy = FixedBackOffPolicy().apply {
      backOffPeriod = intervalMs
    }
    retryTemplate.setBackOffPolicy(backOffPolicy)

    val exceptionMap = exceptions.map { it to true }.toMap()
    val retryPolicy = SimpleRetryPolicy(maxAttempts, exceptionMap)
    retryTemplate.setRetryPolicy(retryPolicy)

    return retryTemplate.execute<T, Throwable> {
      task()
    }
  }
}