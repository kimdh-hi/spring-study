package com.toy.springcacheex.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
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
class RedisTemplateScanTest(
  private val redisTemplate: RedisTemplate<String, Any>,
  private val cacheManager: CacheManager
) {

  @Test
  fun scan() {
    val keys = scanSpaceChannelBySpaceId("space1", 10)
    println(keys)
  }

  @Test
  fun scanUseCase() {
    //given
    val space1 = cacheManager.getCache("space1")!!
    space1.put("ch1", 100)
    space1.put("ch2", 100)
    space1.put("ch3", 50)

    //when
    var availableChannel: String? =  null
    scanSpaceChannelBySpaceId("space1", 10).map {
      val count = redisTemplate.opsForValue().get(it) as Int?
      if(count != null && count < 100) {
        availableChannel = it.split("::")[1]
      }
    }

    //then
    assertNotNull(availableChannel)
    assertEquals("ch3", availableChannel)
  }

  private fun scanSpaceChannelBySpaceId(spaceId: String, count: Long): List<String> {
    val scanOptions = ScanOptions.scanOptions()
      .match("$spaceId::ch*")
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