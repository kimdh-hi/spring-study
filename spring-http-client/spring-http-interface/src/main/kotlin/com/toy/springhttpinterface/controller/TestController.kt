package com.toy.springhttpinterface.controller

import com.toy.springhttpinterface.httpinterface.MyTestApiClient
import com.toy.springhttpinterface.httpinterface.PingPongDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
  private val myTestApiClient: MyTestApiClient,
) {

  @GetMapping("/ping-pong")
  fun pingPong(dto: PingPongDto): PingPongDto {
    return myTestApiClient.pingPong(dto)
  }
}
