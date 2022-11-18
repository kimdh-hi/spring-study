package com.toy.redissondistributedlock.config

import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RedissonConfig(
  private val redisProperty: RedisProperty
) {

  @Bean
  fun redissonClient(): RedissonClient {
    val config = Config().apply {
      useSingleServer()
        .address = String.format("redis://%s:%d", redisProperty.host, redisProperty.port)
    }

    return Redisson.create(config)
  }
}