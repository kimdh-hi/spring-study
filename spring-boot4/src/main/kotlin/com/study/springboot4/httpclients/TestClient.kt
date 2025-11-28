package com.study.springboot4.httpclients

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.service.annotation.GetExchange

interface TestClient {

  @GetExchange("/{id}")
  fun get(@PathVariable id: Long): TestResponse?
}

data class TestResponse(val userId: Long, val title: String)
