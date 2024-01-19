package com.sample.distributedtracingtestservicea.feign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient("chatApiClient", url = "localhost:8082/sample")
interface BServiceClient {

  @PostMapping("/save")
  fun save(@RequestBody request: BServiceSaveRequest): String
}

data class BServiceSaveRequest(
  val data: String
)