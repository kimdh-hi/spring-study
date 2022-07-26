package com.toy.springconditional.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(value = ["cache.redis"], havingValue = true.toString())
class RedisConfig: AbstractCacheConfig() {

  @Bean
  override fun CacheConfigInfo(): CacheConfigInfo = RedisConfigInfo()
}