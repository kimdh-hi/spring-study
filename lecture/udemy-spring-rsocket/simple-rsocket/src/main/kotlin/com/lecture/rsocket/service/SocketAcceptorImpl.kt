package com.lecture.rsocket.service

import io.rsocket.ConnectionSetupPayload
import io.rsocket.RSocket
import io.rsocket.SocketAcceptor
import reactor.core.publisher.Mono

class SocketAcceptorImpl: SocketAcceptor {

  override fun accept(payload: ConnectionSetupPayload, rsocket: RSocket): Mono<RSocket> {
    println("SocketAcceptorImpl.accept called..")
    return Mono.fromCallable { MathService() }
  }
}