package com.lecture.rsocket.service

import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.dto.ResponseDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.Payload
import io.rsocket.RSocket
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Duration

open class MathService: RSocket {

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

  override fun requestStream(payload: Payload): Flux<Payload> {
    val requestDto = ObjectUtils.toObject(payload, RequestDto::class.java)
    return Flux.range(1, 10)
      .map { requestDto.input * it }
      .map { ResponseDto(input = requestDto.input, output = it) }
      .delayElements(Duration.ofSeconds(1))
      .doOnNext { println(it) }
      .map { ObjectUtils.toPayload(it) }
  }
}