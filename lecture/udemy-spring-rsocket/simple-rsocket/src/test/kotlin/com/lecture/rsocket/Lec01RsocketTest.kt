package com.lecture.rsocket

import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.dto.ResponseDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.RSocket
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
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

  @Test
  fun fireAndForget() {
    val payload = ObjectUtils.toPayload(RequestDto(10))
    val mono = rsocket.fireAndForget(payload)
    StepVerifier.create(mono)
      .verifyComplete()
  }

  @Test
  fun requestResponse() {
    val payload = ObjectUtils.toPayload(RequestDto(10))
    val mono = rsocket.requestResponse(payload)
      .map { ObjectUtils.toObject(it, ResponseDto::class.java) }
      .doOnNext { println(it) }

    StepVerifier.create(mono)
      .expectNextCount(1)
      .verifyComplete()
  }
}