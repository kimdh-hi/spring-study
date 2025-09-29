package com.study.openfeign.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.cloud.openfeign.SpringQueryMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@FeignClient(name = "myTestApiClient", url = "http://localhost:8084/test")
interface MyTestApiClient {

  @GetMapping("/ping-pong")
  fun pingPong(@SpringQueryMap dto: PingPongDto): PingPongDto

  @GetMapping("/test3")
  fun test3(@RequestParam ids: List<String>): List<String>
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime,
  val nullableData: String?,
  val list: List<String>,
  val type: String = "AA",
)

