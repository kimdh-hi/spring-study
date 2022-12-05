package com.toy.springcacheex.test

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
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

  @Test
  fun hashUseCase() {
    //given
    val hashOperation = redisTemplate.opsForHash<String, Int>()

    val key = "space01Id"
    val hashKey1 = "space01Channel01Id"
    val hashKey2 = "space01Channel02Id"
    val hashKey3 = "space01Channel03Id"
    hashOperation.put(key, hashKey1, 100)
    hashOperation.put(key, hashKey2, 100)
    hashOperation.put(key, hashKey3, 50)

    //when
    val availableSpaceChannelId = getAvailableSpaceChannelIdOrNull(key)
    renewCache(key, availableSpaceChannelId!!, true)

    //then
    assertNotNull(availableSpaceChannelId)
    assertEquals(hashKey3, availableSpaceChannelId)
    assertEquals(51, hashOperation.get(key, hashKey3))
  }

  @Test
  fun hashUseCase2() {
    //given
    val hashOperation = redisTemplate.opsForHash<String, Int>()

    val spaceId = "space02Id"
    val spaceChannelId = "space01Channel01Id"
    hashOperation.put(spaceId, spaceChannelId, 100)

    //when
    renewCache(spaceId, spaceChannelId, false)

    //then
    assertEquals(99, hashOperation.get(spaceId, spaceChannelId))
  }

  private fun getAvailableSpaceChannelIdOrNull(spaceId: String): String? {
    val hashOperation = redisTemplate.opsForHash<String, Int>()
    val entries = hashOperation.entries(spaceId)

    var availableSpaceChanelId: String? = null
    entries.keys.forEach {
      if(entries[it]!! < 100) {
        availableSpaceChanelId = it
      }
    }
    return availableSpaceChanelId
  }

  private fun renewCache(spaceId: String, spaceChannelId: String, isIncrease: Boolean) {
    val hashOperation = redisTemplate.opsForHash<String, Int>()

    if(isIncrease) {
      hashOperation.increment(spaceId, spaceChannelId, 1)
    } else {
      hashOperation.increment(spaceId, spaceChannelId, -1)
    }
  }
}