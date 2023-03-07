package com.toy.springredisevent

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import java.util.concurrent.TimeUnit

@SpringBootTest
class Test @Autowired constructor(
  private val redisTemplate: RedisTemplate<String, Any>
) {

  @Test
  fun test() {
    redisTemplate.opsForValue().set("test-key", "test-value", 1, TimeUnit.SECONDS)

    Thread.sleep(10000L)
  }
}