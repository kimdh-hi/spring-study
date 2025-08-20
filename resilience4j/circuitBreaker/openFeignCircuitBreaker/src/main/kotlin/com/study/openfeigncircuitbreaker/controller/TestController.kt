package com.study.openfeigncircuitbreaker.controller

import com.study.openfeigncircuitbreaker.feign.TestClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testClient: TestClient,
) {

  @GetMapping("/test")
  fun test(ex: Boolean) = testClient.hello(ex)
}
