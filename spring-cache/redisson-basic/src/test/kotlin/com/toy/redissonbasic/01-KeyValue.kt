package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import reactor.core.publisher.Mono
import reactor.test.StepVerifier

class `01-KeyValue`: BaseTest() {

  @Test
  fun `key-value`() {
    val bucket = client!!.getBucket<String>("test-key")
    val set: Mono<Void> = bucket.set("test-value")

    val get: Mono<Void> = bucket.get()
      .doOnNext { println(it) }
      .then()

    StepVerifier.create(set.concatWith(get))
      .verifyComplete()
  }
}