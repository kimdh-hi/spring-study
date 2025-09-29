package com.study.testcontainer.config

import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig(
  private val cacheProperties: CacheProperties,
) {
  @Bean
  fun cacheManagerBuilderCustomizer(): RedisCacheManagerBuilderCustomizer {
    return RedisCacheManagerBuilderCustomizer { it.withInitialCacheConfigurations(cacheExpiresMap()) }
  }

  private fun cacheExpiresMap(): Map<String, RedisCacheConfiguration> {
    return CacheKeys.TTL.keys
      .associateWith { redisExpiresConfiguration(CacheKeys.TTL[it]!!) }
  }

  private fun redisExpiresConfiguration(duration: Duration): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().contextClassLoader)
      .prefixCacheNameWith(cacheProperties.redis.keyPrefix)
      .entryTtl(duration)
      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()))
  }
}

object CacheKeys {
  const val USER_GET = "getUser"
  val TTL = mapOf(
    USER_GET to Duration.ofMinutes(1)
  )
}
