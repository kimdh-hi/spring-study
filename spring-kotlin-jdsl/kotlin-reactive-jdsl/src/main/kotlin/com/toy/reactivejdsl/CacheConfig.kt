package com.toy.reactivejdsl

import com.toy.reactivejdsl.common.CacheConstants
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import java.time.Duration

@Configuration
@EnableCaching
class CacheConfig {
  @Bean
  fun cacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
    return RedisCacheManagerBuilderCustomizer { builder: RedisCacheManager.RedisCacheManagerBuilder ->
      builder.withInitialCacheConfigurations(
        cacheExpiresMap()
      )
    }
  }

  private fun cacheExpiresMap(): Map<String, RedisCacheConfiguration> {
    val expiresMap: MutableMap<String, RedisCacheConfiguration> = HashMap()
    expiresMap[CacheConstants.COMPANY] = redisExpiresConfiguration(60)
    return expiresMap
  }

  private fun redisExpiresConfiguration(ttlSecond: Long): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().contextClassLoader)
      .entryTtl(Duration.ofSeconds(ttlSecond))
  }
}