package com.toy.springcacheex.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.toy.springcacheex.common.CustomRedisSerializer
import com.toy.springcacheex.common.CacheConstants
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import java.time.Duration

@Configuration
@ConditionalOnProperty(name = ["spring.cache.type"], havingValue = "REDIS")
@EnableCaching
class RedisConfig(
  private val redisProperties: RedisProperties,
  private val customRedisSerializer: CustomRedisSerializer,
) {

  @Bean
  fun cacheManagerBuilderCustomizer(objectMapper: ObjectMapper): RedisCacheManagerBuilderCustomizer {
    return RedisCacheManagerBuilderCustomizer { it
      .withInitialCacheConfigurations(redisExpiresConfigurationMap(objectMapper))
    }
  }

  @Bean
  fun redisTemplate(redisConnectionFactory: RedisConnectionFactory, objectMapper: ObjectMapper): RedisTemplate<String, Any> {
    return RedisTemplate<String, Any>().apply {
      connectionFactory = redisConnectionFactory
      keySerializer = RedisSerializer.string()
      valueSerializer = GenericJackson2JsonRedisSerializer(objectMapper)
    }
  }

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val redisStandaloneConfig = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

    return LettuceConnectionFactory(redisStandaloneConfig)
  }

  private fun redisExpiresConfigurationMap(objectMapper: ObjectMapper) = CacheConstants.expiresMap.keys
    .associateWith { redisExpiresConfiguration(CacheConstants.expiresMap[it]!!, objectMapper) }

  private fun redisExpiresConfiguration(duration: Duration, objectMapper: ObjectMapper): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().contextClassLoader)
      .entryTtl(duration)
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer(objectMapper))
      )
  }
}