package com.toy.springcacheex.test

import com.toy.springcacheex.common.NoArg
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor
import java.io.Serial
import java.io.Serializable

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
  fun deleteByKey() {
    //given
    val hashOperation = redisTemplate.opsForHash<String, String>()
    val key = "space01"
    val hashKey = "space01-ch-01"
    hashOperation.put(key, hashKey, "100")

    //when
    redisTemplate.delete(key)

    //then
    assertNull(hashOperation.get(key, hashKey))
  }

  @Test
  fun deleteByHashKey() {
    //given
    val hashOperation = redisTemplate.opsForHash<String, String>()
    val key = "space01"
    val hashKey1 = "space01-ch-01"
    val hashKey2 = "space01-ch-02"
    hashOperation.put(key, hashKey1, "100")
    hashOperation.put(key, hashKey2, "100")

    //when
    hashOperation.delete(key, hashKey1)

    //then
    assertAll({
      assertNull(hashOperation.get(key, hashKey1))
      assertNotNull(hashOperation.get(key, hashKey2))
    })
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


  @Test
  fun hashUseCase3() {
    //given
    val hashOperation = redisTemplate.opsForHash<String, Any>()

    val spaceId = "space03Id"
    val spaceChannelId = "space03Channel01Id"
    hashOperation.put(spaceId, spaceChannelId, SpaceChannelCount(count = 100, maxCount = 200))

    //when
    val entries = hashOperation.entries(spaceId)

    entries.keys.forEach {
      val spaceChannelCount = entries[it] as SpaceChannelCount
      if(spaceChannelCount.count < spaceChannelCount.maxCount) {
        hashOperation.put(spaceId, spaceChannelId, spaceChannelCount.count+1)
        return
      }
    }

    //then
    assertEquals(101, hashOperation.get(spaceId, spaceChannelId))
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

@NoArg
data class SpaceChannelCount(
  val count:Int,
  val maxCount:Int
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = -2557860745267802422L
  }
}