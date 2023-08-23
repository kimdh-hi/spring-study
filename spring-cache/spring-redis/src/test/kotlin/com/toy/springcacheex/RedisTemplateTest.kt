package com.toy.springcacheex

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate

@SpringBootTest
class RedisTemplateTest @Autowired constructor(
  private val redisTemplate: RedisTemplate<String, Any>
){

  @Test
  fun test() {
    TestVO("a", "b").let {
      redisTemplate.convertAndSend("test", it)
    }
  }
}

data class TestVO(
  val data1: String,
  val data2: String,
)