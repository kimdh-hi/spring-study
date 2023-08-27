package com.lecture.rsocket.service

import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.util.DefaultPayload
import reactor.core.publisher.Flux

class FastProducerService: RSocket {

  override fun requestStream(payload: Payload): Flux<Payload> {
    return Flux.range(1, 1000)
      .map { it.toString() }
      .doOnNext { println(it) }
      .doFinally { println(it) }
      .map { DefaultPayload.create(it) }
  }
}