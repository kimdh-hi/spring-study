package com.toy.testfakeapi

import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/fake/pingPong")
class PingPongApi {

  @PostMapping("/v1")
  fun pingPingV1(@RequestBody dto: PingPongDto) = ResponseEntity.ok(dto)

  @PostMapping("/v2")
  fun pingPingV2(dto: PingPongDto) = ResponseEntity.ok(dto)
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime
)