package com.toy.springehcache.service

import com.toy.springehcache.vo.TestVO
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TestService {
  private val log = LoggerFactory.getLogger(javaClass)

  @Cacheable("CACHE_TEST_VO")
  fun get(): TestVO {
    log.info("get... no cache...")
    return TestVO()
  }
}