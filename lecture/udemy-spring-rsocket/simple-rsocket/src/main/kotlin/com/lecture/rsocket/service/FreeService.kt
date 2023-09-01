package com.lecture.rsocket.service

import io.rsocket.Payload
import io.rsocket.RSocket
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

class FreeService: MathService() {

  override fun fireAndForget(payload: Payload): Mono<Void> {
    return Mono.empty()
  }

  override fun requestResponse(payload: Payload): Mono<Payload> {
    return Mono.empty()
  }

  override fun requestStream(payload: Payload): Flux<Payload> {
    return super.requestStream(payload)
      .take(3)
  }

  override fun requestChannel(payloads: Publisher<Payload>): Flux<Payload> {
    return super.requestChannel(payloads)
      .take(3)
  }
}