package com.toy.springredisevent.config

import com.toy.springredisevent.redis.ExpirationListener
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.cache.CacheProperties
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.core.RedisKeyValueAdapter
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.listener.PatternTopic
import org.springframework.data.redis.listener.RedisMessageListenerContainer
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories
import org.springframework.data.redis.serializer.RedisSerializer

@Configuration
@EnableCaching
@EnableRedisRepositories(enableKeyspaceEvents = RedisKeyValueAdapter.EnableKeyspaceEvents.ON_STARTUP)
@ConditionalOnProperty(name = ["spring.cache.type"], havingValue = "REDIS")
class RedisConfig(private val cacheProperties: CacheProperties) {

  private val log = LoggerFactory.getLogger(javaClass)

  companion object {
    private const val PATTERN = "__keyevent@*__:expired"
  }

  @Bean
  fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, Any> {
    val redisTemplate = RedisTemplate<String, Any>()
    redisTemplate.setConnectionFactory(redisConnectionFactory)
    redisTemplate.keySerializer = RedisSerializer.string()
    redisTemplate.valueSerializer = RedisSerializer.json()
    return redisTemplate
  }

  @Bean
  fun redisMessageListenerContainer(
    redisConnectionFactory: RedisConnectionFactory,
    expirationListener: ExpirationListener,
  ): RedisMessageListenerContainer {
    val redisMessageListenerContainer = RedisMessageListenerContainer()
    redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory)
    redisMessageListenerContainer.addMessageListener(expirationListener, PatternTopic(PATTERN))
    redisMessageListenerContainer.setErrorHandler { e ->
      log.error(
        "There was an error in redis key expiration listener container",
        e
      )
    }
    return redisMessageListenerContainer
  }
}