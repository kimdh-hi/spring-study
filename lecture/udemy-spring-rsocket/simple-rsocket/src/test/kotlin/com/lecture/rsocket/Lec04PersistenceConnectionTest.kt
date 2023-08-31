package com.lecture.rsocket

import com.lecture.rsocket.client.CallbackService
import io.rsocket.RSocket
import io.rsocket.SocketAcceptor
import io.rsocket.core.RSocketClient
import io.rsocket.core.RSocketConnector
import io.rsocket.transport.netty.client.TcpClientTransport
import io.rsocket.util.DefaultPayload
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Lec04PersistenceConnectionTest {

  private lateinit var rSocketClient: RSocketClient

  @BeforeAll
  fun setup() {
//    rsocket = RSocketConnector.create()
//      .acceptor(SocketAcceptor.with(CallbackService()))
//      .connect(TcpClientTransport.create("localhost", 6565))
//      .block()!!

    val socketMono = RSocketConnector.create()
      .connect(TcpClientTransport.create("localhost", 6565))
      .doOnNext { println("connect...") }

    rSocketClient = RSocketClient.from(socketMono)
  }

  //첫번째 request 후 15초간 sleep
  //15초 사이에 server down
  //15초 내에 server 재시작 시 예외발생
  //PersistenceConnection: socket 을 얻기위해 block() 을 사용하지 않도록 함
  @Test
  fun test() {
    val flux1 = rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
      .map { it.dataUtf8 }
      .delayElements(Duration.ofMillis(300))
      .take(10)
      .doOnNext { println(it) }

    StepVerifier.create(flux1)
      .expectNextCount(10)
      .verifyComplete()

    println("sleep...")
    Thread.sleep(15000)
    println("wake up...")

    val flux2 = rSocketClient.requestStream(Mono.just(DefaultPayload.create("")))
      .map { it.dataUtf8 }
      .delayElements(Duration.ofMillis(300))
      .take(10)
      .doOnNext { println(it) }

    StepVerifier.create(flux2)
      .expectNextCount(10)
      .verifyComplete()

  }
}