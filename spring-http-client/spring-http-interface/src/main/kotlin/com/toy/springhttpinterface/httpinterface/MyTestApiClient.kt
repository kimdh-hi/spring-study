package com.toy.springhttpinterface.httpinterface

import com.toy.springhttpinterface.config.QueryMap
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.HttpExchange
import java.time.LocalDateTime

@HttpExchange("http://localhost:8084/test")
interface MyTestApiClient {

  @GetExchange("/ping-pong")
  fun pingPong(@QueryMap dto: PingPongDto): PingPongDto

  @GetExchange("/test3")
  fun test3(@RequestParam(required = false) ids: List<String> = listOf()): List<String>
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime,
  val nullableData: String?,
  val list: List<String>,
  val type: String = "AA",
)
