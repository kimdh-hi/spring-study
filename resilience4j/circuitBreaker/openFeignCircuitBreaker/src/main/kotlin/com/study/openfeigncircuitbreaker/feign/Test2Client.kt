package com.study.openfeigncircuitbreaker.feign

import com.study.openfeigncircuitbreaker.feign.fallback.TestClientFallback
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "test2Client", url = "http://localhost:8084/test", fallbackFactory = TestClientFallback::class)
interface Test2Client {
  @GetMapping("/test1")
  fun test1(@RequestParam status: Int): String
  @GetMapping("/test2")
  fun test2(@RequestParam status: Int): String
}

