package com.toy.springcacheex.test

import com.toy.springcacheex.common.CacheConstants
import com.toy.springcacheex.common.NoArg
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cache.CacheManager
import org.springframework.test.context.TestConstructor
import java.io.Serial
import java.io.Serializable
import java.util.UUID

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Disabled
class SerializerPerformanceTest(
  private val cacheManager: CacheManager
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Test
  fun putTest() {
    val cache = cacheManager.getCache(CacheConstants.TEST_KEY1)!!

    for (i in 0 until 1) {
      log.info("{}", i)
      cache.put(i, SerializerTestVO.of())
    }
  }

  @Test
  fun putGetTest() {
    val cache = cacheManager.getCache(CacheConstants.TEST_KEY1)!!

    for (i in 0 until 30_000) {
      log.info("{}", i)
      cache.put(i, SerializerTestVO.of())
    }

    for (i in 0 until 30_000) {
      log.info("{}", cache.get(i))
    }
  }
}

@NoArg
data class SerializerTestVO(
  val data1: String,
  val data2: String,
  val data3: String,
  val data4: String,
): Serializable {
  companion object {
    @Serial
    private const val serialVersionUID: Long = 6903132114205025296L

    fun of() = SerializerTestVO(
      data1 = UUID.randomUUID().toString(),
      data2 = UUID.randomUUID().toString(),
      data3 = UUID.randomUUID().toString(),
      data4 = UUID.randomUUID().toString(),
    )
  }
}