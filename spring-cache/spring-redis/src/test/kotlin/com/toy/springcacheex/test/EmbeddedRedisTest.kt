package com.toy.springcacheex.test

import com.toy.springcacheex.common.RedisConstants
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.TestConstructor

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class EmbeddedRedisTest(
  private val cacheManager: CacheManager
) {

  @Test
  fun cachePut() {
    assertDoesNotThrow {
      cacheManager.getCache(RedisConstants.TEST_KEY1)?.put("key1", "value1")
    }
  }

  @Test
  fun cacheGet() {
    val cache = cacheManager.getCache(RedisConstants.TEST_KEY2)!!

    cache.put("key2", "value2")
    val get = cache.get("key2")?.get()

    assertAll({
      assertNotNull(get)
      assertEquals(get, "value2")
    })
  }
}