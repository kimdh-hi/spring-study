package com.toy.springwebfluxredis.config

import org.redisson.api.RedissonClient
import org.redisson.spring.cache.RedissonSpringCacheManager
import org.redisson.spring.session.config.EnableRedissonHttpSession
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@EnableCaching
class RedissonConfig {

  @Bean
  fun cacheManager(redissonClient: RedissonClient): CacheManager
    = RedissonSpringCacheManager(redissonClient)
}