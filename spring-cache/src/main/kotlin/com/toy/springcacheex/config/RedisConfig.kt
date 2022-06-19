package com.toy.springcacheex.config

import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer

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
      .build()
  }

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val redisStandaloneConfig
      = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

    return LettuceConnectionFactory(redisStandaloneConfig) // 비동기
  }
}