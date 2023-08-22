package com.lecture.rsocket

import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.TestInstance
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec01RsocketTest {

  lateinit var rsocket: RSocket

  @BeforeAll
  fun setup() {
    rsocket = RSocketConnector.create()
      .connect(TcpClientTransport.create("localhost", 6565))
      .block()!!
  }

  @RepeatedTest(3)
  fun fireAndForget() {
    val payload = DefaultPayload.create("hello world")
    val mono = rsocket.fireAndForget(payload)
    StepVerifier.create(mono)
      .verifyComplete()
  }
}