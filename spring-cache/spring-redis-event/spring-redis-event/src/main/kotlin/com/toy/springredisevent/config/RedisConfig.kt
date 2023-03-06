package com.toy.springredisevent.config

import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
@EnableCaching
@ConditionalOnProperty(name = ["spring.cache.type"], havingValue = "REDIS")
class RedisConfig(private val cacheProperties: CacheProperties) {

    @Bean
    fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
      val redisTemplate = RedisTemplate<String, Any>()
      redisTemplate.setConnectionFactory(redisConnectionFactory)
      redisTemplate.keySerializer = RedisSerializer.string()
      redisTemplate.valueSerializer = RedisSerializer.json()
      return redisTemplate
    }

  }