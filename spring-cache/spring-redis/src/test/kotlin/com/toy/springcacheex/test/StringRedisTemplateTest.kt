package com.toy.springcacheex.test

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springcacheex.domain.User
import com.toy.springcacheex.domain.enums.UserStatus
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.test.context.TestConstructor
import java.time.LocalDateTime

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class StringRedisTemplateTest(
  private val redisTemplate: StringRedisTemplate,
  private val objectMapper: ObjectMapper
) {

  @Test
  fun `value get-set test`() {
    val ops = redisTemplate.opsForValue()
    ops.set("test-key1", "tests-value1")
    ops.set("test-key2", "tests-value2")
  }

  @Test
  fun `object get-set test`() {
    val ops = redisTemplate.opsForValue()

    val user = User(id = "user1", username = "user1", status = UserStatus.ENABLED, createdDate = LocalDateTime.now())
    ops.set("user:1", objectMapper.writeValueAsString(user))

    val getValue = objectMapper.readValue(ops.get("user:1"), User::class.java)
    println(getValue)
  }
}