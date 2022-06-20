package com.toy.redisson.test.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.api.RedissonReactiveClient
import org.redisson.config.Config

class RedissonConfig(val redissonClient: RedissonClient?) {

  fun getClient(): RedissonClient {
    return redissonClient ?: run {
      val config = Config()
      config.useSingleServer()
         .setAddress("redis://127.0.0.1:6379")
      Redisson.create(config)
    }
  }

  fun getReactiveClient(): RedissonReactiveClient {
    return getClient().reactive()
  }
}