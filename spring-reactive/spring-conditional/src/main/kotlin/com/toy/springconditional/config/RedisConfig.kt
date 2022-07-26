package com.toy.springconditional.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/*
cache.redis 가 true 인 경우 동작하는 Configuration
 */
@Configuration
@ConditionalOnProperty(value = ["cache.redis"], havingValue = true.toString())
class RedisConfig: AbstractCacheConfig() {

  @Bean
  override fun CacheConfigInfo(): CacheConfigInfo = RedisConfigInfo()
}