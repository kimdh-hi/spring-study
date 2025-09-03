package com.study.resilience4j.httpclient

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component

@Component
class Test2Client(
  private val test2Exchange: Test2Exchange,
) : Test2Exchange by test2Exchange {

  @CircuitBreaker(name = "test2", fallbackMethod = "testFallback")
  override fun test1(status: Int): String {
    return test2Exchange.test1(status)
  }

  private fun testFallback(status: Int, throwable: Throwable): String {
    return "test fallback status=$status, throwable.message=${throwable.message}"
  }
}
