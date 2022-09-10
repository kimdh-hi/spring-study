package com.toy.redissonbasic.base

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig {

  var redissonClient: RedissonClient? = null

  fun getClient(): RedissonClient {
    return redissonClient?.let {
      redissonClient
    } ?: run {
      val config = Config().apply {
        useSingleServer()
          .address = "redis://127.0.0.1:6379"
      }
      Redisson.create(config)
    }
  }

  fun getReactiveClient(): RedissonReactiveClient {
    return getClient().reactive()
  }
}