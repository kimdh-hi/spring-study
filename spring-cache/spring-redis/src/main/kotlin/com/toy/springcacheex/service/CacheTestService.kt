package com.toy.springcacheex.service

import com.toy.springcacheex.common.RedisConstants
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

@Service
class CacheTestService {

  private val log = LoggerFactory.getLogger(javaClass)

  @Cacheable(value = [RedisConstants.TEST_KEY], key = "#spaceId")
  fun getSpaceParticipantCount(spaceId: String, spaceChannelId: String): String {
    log.info("no cache..")
    return "100"
  }
}