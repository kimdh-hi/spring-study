package com.study.resilience4j.httpclient

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Service

@Service
class TestClient(
  private val testExchange: TestExchange,
) : TestExchange by testExchange {

  @CircuitBreaker(name = "test1", fallbackMethod = "testFallback")
  override fun test1(status: Int): String = testExchange.test1(status)

  private fun testFallback(status: Int, throwable: Throwable): String {
    return "test fallback"
  }
}
