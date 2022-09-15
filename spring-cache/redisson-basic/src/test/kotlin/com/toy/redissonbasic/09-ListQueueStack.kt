package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.client.codec.LongCodec
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.stream.Collectors
import java.util.stream.LongStream

class `09-ListQueueStack`: BaseTest() {

  @Test
  fun test() {
    val list = client!!.getList<Long>("test-list", LongCodec.INSTANCE)
//    val longList = Flux.range(1, 10)
//      .map { it.toLong() }
//      .flatMap { list.add(it) }
//      .then()

    val longList = LongStream.rangeClosed(1, 10)
      .boxed()
      .collect(Collectors.toList())

    StepVerifier.create(list.addAll(longList))
      .verifyComplete()
    StepVerifier.create(list.size())
      .expectNext(10)
      .verifyComplete()
  }
}