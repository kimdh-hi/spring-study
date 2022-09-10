package com.toy.redissonbasic

import com.toy.redissonbasic.base.BaseTest
import org.junit.jupiter.api.Test
import org.redisson.client.codec.StringCodec
import reactor.test.StepVerifier

class `04-Get-MapType`: BaseTest() {

  @Test
  fun `getMultiKey`() {
    // 한번에 여러 key 에 해당하는 value 를 get
    // map 타입으로 반환됨
    // key 가 존재하지 않아도 예외는 발생하지 않고 무시됨
    val mono = client!!.getBuckets(StringCodec.INSTANCE)
      .get<String>(
        "user:1:username", "user:2:username", "user:3:username",
        "user:4:username" // 존재하지 않는 key
      )
      .doOnNext { println(it) }
      .then()
    StepVerifier.create(mono)
      .verifyComplete()

  }
}