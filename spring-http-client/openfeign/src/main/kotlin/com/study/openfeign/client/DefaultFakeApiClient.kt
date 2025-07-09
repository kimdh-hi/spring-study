package com.study.openfeign.client

import com.study.openfeign.config.TestFeignClientConfig
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "defaultFakeApiClient", url = "https://jsonplaceholder.typicode.com", configuration = [TestFeignClientConfig::class])
interface DefaultFakeApiClient {

  @GetMapping("/todos/1")
  fun getTodo(): TodoResponse
}
