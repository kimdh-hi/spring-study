package com.study.resilience4j.httpclient

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange

interface Test2Client {

  @GetExchange("/test/test1")
  @CircuitBreaker(name = "test2", fallbackMethod = "fallback")
  fun test1(@RequestParam status: Int): String

  fun fallback(status: Int, ex: Throwable): String {
    return "/test/test1 fallback"
  }

  @GetExchange("/test/test2")
  @CircuitBreaker(name = "test2")
  fun test2(@RequestParam status: Int): String
}

