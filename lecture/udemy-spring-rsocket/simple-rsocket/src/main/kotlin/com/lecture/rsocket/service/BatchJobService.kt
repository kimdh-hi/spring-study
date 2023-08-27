package com.lecture.rsocket.service

import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.dto.ResponseDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.Payload
import io.rsocket.RSocket
import reactor.core.publisher.Mono
import java.time.Duration

class BatchJobService(
  private val rSocket: RSocket
): RSocket {

  override fun fireAndForget(payload: Payload): Mono<Void> {
    val requestDto = ObjectUtils.toObject(payload, RequestDto::class.java)
    println("fireAndForget received payload: $requestDto")

    Mono.just(requestDto)
      .delayElement(Duration.ofSeconds(10))
      .doOnNext { println("emitting...") }
      .flatMap { findCube(it) }
      .subscribe()

    return Mono.empty()
  }

  private fun findCube(requestDto: RequestDto): Mono<Void> {
    val input = requestDto.input
    val output = input * input * input
    val responseDto = ResponseDto(input = requestDto.input, output = output)
    val payload = ObjectUtils.toPayload(responseDto)
    return rSocket.fireAndForget(payload)
  }
}