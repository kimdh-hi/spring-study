package com.study.openfeigncircuitbreaker.controller

import com.study.openfeigncircuitbreaker.feign.Test2Client
import com.study.openfeigncircuitbreaker.feign.TestClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val testClient: TestClient,
  private val test2Client: Test2Client,
) {

  @GetMapping("/test/1")
  fun test1(status: Int) = testClient.test1(status)

  @GetMapping("/test/2")
  fun test2(status: Int) = testClient.test2(status)

  @GetMapping("/test/3")
  fun test3(status: Int) = test2Client.test1(status)

  @GetMapping("/test/4")
  fun test4(status: Int) = testClient.test2(status)

  @GetMapping("/test/without-fallback")
  fun testWithoutFallback(status: Int) = testClient.withoutFallback(status)
}
