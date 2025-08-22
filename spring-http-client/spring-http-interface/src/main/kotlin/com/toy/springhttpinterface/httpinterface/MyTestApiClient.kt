package com.toy.springhttpinterface.httpinterface

import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import java.time.LocalDateTime

@HttpExchange("http://localhost:8084/test")
interface MyTestApiClient {

  @GetExchange("/ping-pong")
  fun pingPong(dto: PingPongDto): PingPongDto
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime,
  val nullableData: String?,
  val list: List<String>,
  val type: String = "AA",
)
