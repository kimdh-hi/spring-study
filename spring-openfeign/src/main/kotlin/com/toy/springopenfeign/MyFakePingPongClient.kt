package com.toy.springopenfeign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import java.time.LocalDateTime

@FeignClient(url = "\${open-feign.my-fake-url}/pingPong", name = "myFakePingPongClient")
interface MyFakePingPongClient {

  @PostMapping("/v1")
  fun pingPongV1(dto: PingPongDto): PingPongDto

  @PostMapping("/v2")
  fun pingPongV2(dto: PingPongDto): PingPongDto
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime
)