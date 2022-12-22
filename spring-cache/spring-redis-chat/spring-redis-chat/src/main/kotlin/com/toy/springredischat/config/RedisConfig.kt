package com.toy.springredischat.config

import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.RedisMessageListenerContainer

@Configuration
class RedisConfig(
  private val redisProperties: RedisProperties
) {

  @Bean
  fun redisMessageListenerContainer(): RedisMessageListenerContainer {
    return RedisMessageListenerContainer().apply {
      setConnectionFactory(redisConnectionFactory())
    }
  }

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val redisStandaloneConfig
      = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

    return LettuceConnectionFactory(redisStandaloneConfig)
  }

  @Bean
  fun redisTemplate(): RedisTemplate<String, Any> {
    val redisTemplate = RedisTemplate<String, Any>()
    redisTemplate.setConnectionFactory(redisConnectionFactory())
    return redisTemplate
  }
}