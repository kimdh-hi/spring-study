package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.time.Duration
import java.util.concurrent.TimeUnit

class `01-KeyValue`: BaseTest() {

  @Test
  fun `key-value`() {
    val bucket = client!!.getBucket<String>("test-key", StringCodec.INSTANCE)
    val set: Mono<Void> = bucket.set("test-value")

    val get: Mono<Void> = bucket.get()
      .doOnNext { println(it) }
      .then()

    StepVerifier.create(set.concatWith(get))
      .verifyComplete()
  }

  @Test
  fun `ttl 설정`() {
    val bucket = client!!.getBucket<String>("ttl-test-key", StringCodec.INSTANCE)
    //ttl 설정
    val set = bucket.set("test-value", 10, TimeUnit.SECONDS)

    val get = bucket.get()
      .doOnNext { println(it) }
      .then()

    StepVerifier.create(set.concatWith(get))
      .verifyComplete()

    Thread.sleep(5000)

    // ttl 연장
    val extendsResult = bucket.expire(Duration.ofSeconds(30))
    StepVerifier.create(extendsResult)
      .expectNext(true)
      .verifyComplete()

    // ttl 확인
    val ttl = bucket.remainTimeToLive()
      .doOnNext { println(it) }
      .then()
    StepVerifier.create(ttl)
      .verifyComplete()
  }
}