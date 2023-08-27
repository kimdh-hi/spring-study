package com.lecture.rsocket

import com.lecture.rsocket.client.CallbackService
import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.RSocket
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.test.StepVerifier

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec02CallbackTest {

  lateinit var rsocket: RSocket

  @BeforeAll
  fun setup() {
    rsocket = RSocketConnector.create()
      .acceptor(SocketAcceptor.with(CallbackService()))
      .connect(TcpClientTransport.create("localhost", 6565))
      .block()!!
  }

  @Test
  fun callback() {
    val requestDto = RequestDto(10)
    val mono = rsocket.fireAndForget(ObjectUtils.toPayload(requestDto))

    StepVerifier.create(mono)
      .verifyComplete()

    Thread.sleep(12000)
  }
}