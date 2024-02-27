package com.toy.springcacheex.service

import com.toy.springcacheex.common.CacheConstants
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.io.Serial
import java.io.Serializable
import java.time.LocalDateTime

@Service
class CacheableService {

  private val log = LoggerFactory.getLogger(javaClass)

  @Cacheable(value = [CacheConstants.TEST_KEY4])
  fun business(): CacheableVO {
    log.info("no cache...")
    return CacheableVO("data1")
  }
}

data class CacheableVO(
  val data1: String,
  val date: LocalDateTime = LocalDateTime.now()
): Serializable {
//  constructor(): this("")

  companion object {
    @Serial
    private const val serialVersionUID: Long = -2681223834424762047L
  }
}
