package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.time.Duration

class `03-Increase-value`: BaseTest() {

  @Test
  fun `valueIncreasing`() {
    val visitCount = client!!.getAtomicLong("user:1:visit")
    val mono = Flux.range(1, 10)
      .delayElements(Duration.ofSeconds(1))
      .flatMap { visitCount.incrementAndGet() }
      .then()
    StepVerifier.create(mono)
      .verifyComplete()
  }

}