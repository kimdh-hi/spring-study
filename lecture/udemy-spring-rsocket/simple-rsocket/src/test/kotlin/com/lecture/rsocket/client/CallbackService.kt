package com.lecture.rsocket.client

import com.lecture.rsocket.dto.ResponseDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.Payload
import io.rsocket.RSocket
import reactor.core.publisher.Mono

class CallbackService: RSocket {

  override fun fireAndForget(payload: Payload): Mono<Void> {
    println("client received: ${ObjectUtils.toObject(payload, ResponseDto::class.java)}")
    return Mono.empty()
  }
}