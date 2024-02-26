package com.toy.springcacheex

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import java.time.LocalDateTime

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

  @Test
  fun `직렬화 역직렬화 test`() {
    val nestedVO = NestedTestVO("a", "b")
    val vo = TestVO("a", "b", nestedVO)
    redisTemplate.opsForValue()["testKey1"] = vo

    val deserializedVO = redisTemplate.opsForValue().get("testKey1")
    println(deserializedVO)
  }
}

data class TestVO(
  val data1: String,
  val data2: String,
  val nestedVO: NestedTestVO? = null,
  val createdDate: LocalDateTime = LocalDateTime.now(),
)

data class NestedTestVO(
  val data1: String,
  val data2: String,
  val createdDate: LocalDateTime = LocalDateTime.now(),
)