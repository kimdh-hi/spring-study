package com.study.resilience4j.httpclient

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.stereotype.Component

@Component
class Test1Client(
  private val test1Exchange: Test1Exchange,
) : Test1Exchange by test1Exchange {

  @CircuitBreaker(name = "test1", fallbackMethod = "fallback")
  override fun test1(status: Int): String = test1Exchange.test1(status)

  private fun fallback(status: Int, ex: Throwable): String {
    return "/test/test1 fallback"
  }
}



