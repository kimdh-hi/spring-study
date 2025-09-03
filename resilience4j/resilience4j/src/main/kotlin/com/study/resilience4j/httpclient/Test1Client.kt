package com.study.resilience4j.httpclient

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component

@Component
class Test1Client(
  private val test1Exchange: Test1Exchange,
) : Test1Exchange by test1Exchange {

  @CircuitBreaker(name = "test1", fallbackMethod = "testFallback")
  override fun test1(status: Int): String {
    return test1Exchange.test1(status)
  }

  private fun testFallback(status: Int, throwable: Throwable): String {
    return "test fallback status=$status, throwable.message=${throwable.message}"
  }
}
