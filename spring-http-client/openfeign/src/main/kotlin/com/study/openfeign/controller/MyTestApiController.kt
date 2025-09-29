package com.study.openfeign.controller

import com.study.openfeign.client.MyTestApiClient
import com.study.openfeign.client.PingPongDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MyTestApiController(
  private val myTestApiClient: MyTestApiClient,
) {

  @GetMapping("/ping-pong")
  fun pingPong(dto: PingPongDto): PingPongDto {
    return myTestApiClient.pingPong(dto)
  }

  @GetMapping("/test3")
  fun test3() = myTestApiClient.test3(listOf())
}
