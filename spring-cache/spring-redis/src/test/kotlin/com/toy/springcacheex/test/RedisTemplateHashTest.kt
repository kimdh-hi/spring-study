package com.toy.springcacheex.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisTemplateHashTest(
  private val redisTemplate: RedisTemplate<String, Any>
) {

  @Test
  fun hash() {
    val hashOperation = redisTemplate.opsForHash<String, String>()

    val key = "space01"
    val hashKey1 = "space01-ch-01"
    val hashKey2 = "space01-ch-02"
    hashOperation.put(key, hashKey1, "100")
    hashOperation.put(key, hashKey2, "50")

    hashOperation.values(key).forEach {
      println(it)
    }
  }
}