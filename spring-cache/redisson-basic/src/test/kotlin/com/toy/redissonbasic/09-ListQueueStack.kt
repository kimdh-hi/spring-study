package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier

class `09-ListQueueStack`: BaseTest() {

  @Test
  fun test() {
    val list = client!!.getList<Long>("test-list", LongCodec.INSTANCE)
    val listAdd = Flux.range(1, 10)
      .map { it.toLong() }
      .flatMap { list.add(it) }
      .then()

    StepVerifier.create(listAdd)
      .verifyComplete()
    StepVerifier.create(list.size())
      .expectNext(10)
      .verifyComplete()
  }
}