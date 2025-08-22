package com.toy.springhttpinterface.httpinterface

import org.springframework.web.service.annotation.GetExchange
import java.time.LocalDateTime

interface MyTestApiClient {

  @GetExchange("/ping-pong")
  fun pingPong(dto: PingPongDto): PingPongDto
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime
)
