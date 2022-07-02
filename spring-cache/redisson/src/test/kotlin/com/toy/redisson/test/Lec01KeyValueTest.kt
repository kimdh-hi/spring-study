package com.toy.redisson.test

import org.junit.jupiter.api.Test
import reactor.test.StepVerifier

class Lec01KeyValueTest: BaseTest() {

  @Test
  fun keyValueAccessTest() {
    val nameBucket = client.getBucket<String>("user:1:name")
    val set = nameBucket.set("kim")

    val get = nameBucket.get()
      .doOnNext {
        println(it)
      }
      .then()

    StepVerifier.create(set.concatWith(get))
      .verifyComplete()
  }

}