package com.study.openfeign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "fakeApiClient", url = "https://jsonplaceholder.typicode.com")
interface FakeApiFeignClient {

  @GetMapping("/todos/1")
  fun getTodo(): TodoResponse
}

data class TodoResponse(val userId: Int, val id: Int, val title: String, val completed: Boolean)
