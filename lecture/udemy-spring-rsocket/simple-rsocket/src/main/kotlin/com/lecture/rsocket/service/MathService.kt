package com.lecture.rsocket.service

import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.dto.ResponseDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.Payload
import io.rsocket.RSocket
import reactor.core.publisher.Mono

class MathService: RSocket {

  override fun fireAndForget(payload: Payload): Mono<Void> {
    println("receive: ${payload.dataUtf8}")

    return Mono.empty()
  }

  override fun requestResponse(payload: Payload): Mono<Payload> {
    return Mono.fromSupplier {
      val requestDto = ObjectUtils.toObject(payload, RequestDto::class.java)
      val responseDto = ResponseDto(input = requestDto.input, output = requestDto.input * requestDto.input)
      ObjectUtils.toPayload(responseDto)
    }
  }
}