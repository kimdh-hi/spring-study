package com.toy.springehcache.service

import com.toy.springehcache.vo.TestVO
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class TestService {
  private val log = LoggerFactory.getLogger(javaClass)

  @Cacheable(value = ["TEST_CACHE1"], key = "#data")
  fun get1(data: String): String {
    Thread.sleep(1000L)
    log.info("get... no cache...")
    return data
  }

  @CacheEvict(value = ["TEST_CACHE2"])
  fun evict1(data: String): String {
    return data
  }

  @Cacheable(value = ["TEST_CACHE2"], key = "#testVO")
  fun get2(testVO: TestVO): TestVO {
    Thread.sleep(1000L)
    log.info("get... no cache...")
    return testVO
  }

  @CacheEvict(value = ["TEST_CACHE2"], key = "#testVO")
  fun evict2(testVO: TestVO): TestVO {
    return testVO
  }
}