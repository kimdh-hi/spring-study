package com.lecture.rsocket.service

import io.rsocket.Payload
import io.rsocket.RSocket
import reactor.core.publisher.Mono

class MathService: RSocket {

  override fun fireAndForget(payload: Payload): Mono<Void> {
    println("receive: ${payload.dataUtf8}")

    return Mono.empty()
  }
}