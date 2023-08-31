package com.lecture.rsocket

import com.lecture.rsocket.client.CallbackService
import com.lecture.rsocket.dto.RequestDto
import com.lecture.rsocket.utils.ObjectUtils
import io.rsocket.Payload
import io.rsocket.RSocket
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.test.StepVerifier
import reactor.util.concurrent.Queues
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec03BackpressureTest {

  private lateinit var rsocket: RSocket

  @BeforeAll
  fun setup() {
    rsocket = RSocketConnector.create()
      .acceptor(SocketAcceptor.with(CallbackService()))
      .connect(TcpClientTransport.create("localhost", 6565))
      .block()!!
  }

  @Test
  fun backpressure() {
    // default=32, minimum=8 (reactor.util.concurrent.Queues.XS_BUFFER_SIZE)
    // 구독자가 매우 느린 경우 발행자는 기본적으로 32개 아이템만 발행
    // 구독자가 75% 정도를 소비한 경우 다시 32개 아이템 발행
    val flux = rsocket.requestStream(DefaultPayload.create(""))
      .map { it.dataUtf8 }
      .delayElements(Duration.ofSeconds(1))
      .doOnNext { println(it) }

    StepVerifier.create(flux)
      .expectNextCount(1000)
      .verifyComplete()
  }
}