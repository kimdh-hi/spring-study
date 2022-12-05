package com.toy.springcacheex.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.core.RedisTemplate

@Configuration
class RedisTestConfig(
  private val redisTemplate: RedisTemplate<String, String>
) {

  fun init() {

  }
}