package com.study.openfeign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(name = "fakeApiClient", url = "https://jsonplaceholder.typicode.com")
interface FakeApiFeignClient {

  @GetMapping("/todos")
  fun getTodos(): List<TodoResponse>
}

data class TodoResponse(val userId: Int, val id: Int, val title: String, val completed: Boolean)
