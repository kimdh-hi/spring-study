package com.toy.springcacheex.test

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ScanOptions
import org.springframework.test.context.TestConstructor

/**
 * redis scan
 *
 * keys 대체
 * keys => O(N)
 * 모든 키를 한 번에 가져오는 것이 문제 (키가 많아질수록 큰 부하)
 *
 * scan 은 커서기반으로 n개 만큼의 key 만을 가져올 수 있음
 * keys 와 마찬가지로 패턴매칭(*...) 또한 사용 가능
 */

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisTemplateTest(
  private val redisTemplate: RedisTemplate<String, Any>
) {

  @Test
  fun scan() {
    val keys = scanSpaceChannelBySpaceId("space1", 10)

    println(keys)
  }

  private fun scanSpaceChannelBySpaceId(spaceId: String, count: Long): List<String> {
    val scanOptions = ScanOptions.scanOptions()
      .match("$spaceId:ch*")
      .count(count)
      .build()

    val keys = mutableListOf<String>()

    redisTemplate.connectionFactory?.connection?.scan(scanOptions)?.let {
      while (it.hasNext()) {
        val key = it.next()
        keys.add(String(key, Charsets.UTF_8))
      }
    }

    return keys.toList()
  }
}