package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.redisson.api.RBlockingDequeReactive
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class `10-MessageQueue`: BaseTest() {

  var messageQueue: RBlockingDequeReactive<Long>? = null

  @BeforeAll
  fun setup() {
    messageQueue = client!!.getBlockingDeque<Long>("message-queue", LongCodec.INSTANCE)
  }

  @Test
  fun consumer1() {
    messageQueue!!.takeElements()
      .doOnNext { println("Consumer1 : $it") }
      .doOnError { error -> println("Consumer1 error... $error") }
      .subscribe()
    Thread.sleep(600_000)
  }

  @Test
  fun consumer2() {
    messageQueue!!.takeElements()
      .doOnNext { println("Consumer2 : $it") }
      .doOnError { error -> println("Consumer2 error... $error") }
      .subscribe()
    Thread.sleep(600_000)
  }

  @Test
  fun producer() {
    val producer = Flux.range(1, 100)
      .delayElements(Duration.ofSeconds(1))
      .doOnNext { println("produce: $it") }
      .flatMap { messageQueue!!.add(it.toLong()) }
      .then()

    StepVerifier.create(producer)
      .verifyComplete()
  }
}