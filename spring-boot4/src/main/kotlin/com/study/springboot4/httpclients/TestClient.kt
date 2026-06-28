package com.study.springboot4.httpclients

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange

interface TestClient {

  @GetExchange("/{id}")
  @CircuitBreaker(name = "test-circuit")
  fun get(@PathVariable("id") id: Long): TestResponse?
}

data class TestResponse(
  val userId: Long,
  val id: Long,
  val title: String,
  val completed: Boolean
)
