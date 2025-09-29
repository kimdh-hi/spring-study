package com.toy.testfakeapi

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.RequestEntity
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime

@RestController
@RequestMapping("/test/ping-pong")
class PingPongApi {

  private val log = LoggerFactory.getLogger(javaClass)

  @PostMapping("/v1")
  fun pingPingV1(@RequestBody dto: PingPongDto, request: HttpServletRequest): ResponseEntity<PingPongDto> {
    val userId = request.getHeader("userId")
    val userId2 = request.getHeader("userId2")
    log.info("[userId=$userId]")
    log.info("[userId2=$userId2]")
    return ResponseEntity.ok(dto)
  }

  @PostMapping("/v2")
  fun pingPingV2(dto: PingPongDto) = ResponseEntity.ok(dto)

  @GetMapping
  fun pingPong(dto: PingPongDto) = dto
}

data class PingPongDto(
  val data: String,
  val date: LocalDateTime,
  val nullableData: String?,
  val list: List<String>,
  val type: PingPongType,
)

enum class PingPongType {
  AA, BB
}
