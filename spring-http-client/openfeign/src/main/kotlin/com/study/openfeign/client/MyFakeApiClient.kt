package com.study.openfeign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "myFakeApiClient", url = "http://fake.api.local:8084")
interface MyFakeApiClient {

  @GetMapping("/fake/sleeps")
  fun sleep(): Unit
}
