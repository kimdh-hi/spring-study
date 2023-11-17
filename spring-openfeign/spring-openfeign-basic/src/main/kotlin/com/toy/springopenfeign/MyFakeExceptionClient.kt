package com.toy.springopenfeign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(url = "\${open-feign.my-fake-url}/exception", name = "myFakeExceptionClient")
interface MyFakeExceptionClient {

  @GetMapping
  fun exception(): String
}