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
  fun list() {
    val list = client!!.getList<Long>("test-list", LongCodec.INSTANCE)
//    val longList = Flux.range(1, 10)
//      .map { it.toLong() }
//      .flatMap { list.add(it) }
//      .then()

    val longList = LongStream.rangeClosed(1, 10)
      .boxed()
      .collect(Collectors.toList())

    StepVerifier.create(list.addAll(longList).then())
      .verifyComplete()
    StepVerifier.create(list.size())
      .expectNext(10)
      .verifyComplete()
  }

  @Test
  fun queue() {
    val queue = client!!.getQueue<Long>("test-list", LongCodec.INSTANCE)
    val pollMono = queue.poll()
      .repeat(3)
      .doOnNext { println(it) }
      .then()

    StepVerifier.create(pollMono)
      .verifyComplete()
  }

  @Test
  fun `stack-deque`() {
    val queue = client!!.getDeque<Long>("test-list", LongCodec.INSTANCE)
    val pollMono = queue.pollLast()
      .repeat(3)
      .doOnNext { println(it) }
      .then()

    StepVerifier.create(pollMono)
      .verifyComplete()
  }
}
