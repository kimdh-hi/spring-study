package com.study.resilience4j.controller

import com.study.resilience4j.httpclient.TestClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testClient: TestClient,
) {

  @GetMapping("/test/1")
  fun test1(status: Int) = testClient.test1(status)

  @GetMapping("/test/2")
  fun test2(status: Int) = testClient.test2(status)
}

