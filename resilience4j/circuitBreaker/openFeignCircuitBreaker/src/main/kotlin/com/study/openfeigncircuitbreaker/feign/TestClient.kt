package com.study.openfeigncircuitbreaker.feign

import com.study.openfeigncircuitbreaker.feign.fallback.TestClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "testClient", url = "http://localhost:8084/test", fallbackFactory = TestClientFallback::class)
interface TestClient {
  @GetMapping("/hello")
  fun hello(@RequestParam exception: Boolean): String

  @GetMapping("/hello2")
  fun hello2(@RequestParam exception: Boolean): String
}
