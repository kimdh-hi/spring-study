package com.toy.springrsocket

import com.toy.springrsocket.dto.ComputationRequestDto
import io.rsocket.transport.netty.client.TcpClientTransport
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import reactor.test.StepVerifier

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec01RsocketTest @Autowired constructor(
  private val builder: RSocketRequester.Builder
) {

  lateinit var requester: RSocketRequester

  @BeforeAll
  fun setup() {
    requester = builder.transport(TcpClientTransport.create("localhost", 6565))
  }

  @Test
  fun fireAndForget() {
    val mono = requester.route("math.service.print")
      .data(ComputationRequestDto(10))
      .send()

    StepVerifier.create(mono)
      .verifyComplete()
  }
}