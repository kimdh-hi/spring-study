package com.toy.springcacheex.test

import com.fasterxml.jackson.databind.ObjectMapper
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
) {

  @Test
  fun `value get-set test`() {
    val ops = redisTemplate.opsForValue()
    ops.set("test-key1", "tests-value1")
    ops.set("test-key2", "tests-value2")
  }

}