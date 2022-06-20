package com.toy.springcacheex.config

import com.toy.springcacheex.common.RedisConstants
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig(private val redisProperties: RedisProperties) {

  @Bean
  fun redisCacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {

    val redisCacheDefaultConfig = RedisCacheConfiguration.defaultCacheConfig()
      .disableCachingNullValues() // null value does not caching...
      .serializeKeysWith(
        RedisSerializationContext.SerializationPair.fromSerializer(
          StringRedisSerializer()
        )
      )
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(
          GenericJackson2JsonRedisSerializer()
        )
      )

    return RedisCacheManager.RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(redisCacheDefaultConfig)
      .withInitialCacheConfigurations(ttlInfoMap()) // ttl user::id-1
      .build()
  }

  @Bean
  fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
    val redisTemplate = RedisTemplate<String, Any>()
    redisTemplate.setConnectionFactory(redisConnectionFactory)
    redisTemplate.keySerializer = StringRedisSerializer()
    redisTemplate.valueSerializer = GenericJackson2JsonRedisSerializer()
    return redisTemplate
  }

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val redisStandaloneConfig
      = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

    return LettuceConnectionFactory(redisStandaloneConfig) // 비동기
  }

  private fun ttlInfoMap() = mutableMapOf(
    RedisConstants.USER to redisExpiresConfiguration(RedisConstants.USER_TTL)
  )

  private fun redisExpiresConfiguration(ttl: Long): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().contextClassLoader)
      .entryTtl(Duration.ofSeconds(ttl))
  }
}