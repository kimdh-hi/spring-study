package com.toy.springcacheex.config

import com.toy.springcacheex.common.CustomRedisSerializer
import com.toy.springcacheex.common.RedisConstants
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
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
@ConditionalOnProperty(value = ["cache.cache-name"], havingValue = "redis")
@EnableCaching
class RedisConfig(
  private val redisProperties: RedisProperties,
  private val customRedisSerializer: CustomRedisSerializer,
) {

  private val log = LoggerFactory.getLogger(javaClass)

  @Bean
  fun cacheManager(redisConnectionFactory: RedisConnectionFactory): RedisCacheManager {
    log.info("redis cacheManager bean register")
    return RedisCacheManager.RedisCacheManagerBuilder
      .fromConnectionFactory(redisConnectionFactory)
      .cacheDefaults(generateCacheConfig())
      .withInitialCacheConfigurations(redisExpiresConfigurationMap())
      .build()
  }

  private fun generateCacheConfig(): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer<Any>(
          customRedisSerializer
        )
      )
  }

  @Bean
  fun redisTemplate(redisConnectionFactory: RedisConnectionFactory): RedisTemplate<String, String> {
    val redisTemplate = RedisTemplate<String, String>()
    redisTemplate.setConnectionFactory(redisConnectionFactory)
    redisTemplate.valueSerializer = CustomRedisSerializer()
    redisTemplate.afterPropertiesSet()
    return redisTemplate
  }

  @Bean
  fun redisConnectionFactory(): RedisConnectionFactory {
    val redisStandaloneConfig
      = RedisStandaloneConfiguration(redisProperties.host, redisProperties.port)

    return LettuceConnectionFactory(redisStandaloneConfig)
  }

  private fun redisExpiresConfigurationMap() = mutableMapOf(
    RedisConstants.TEST_KEY1 to redisExpiresConfiguration(600L),
    RedisConstants.TEST_KEY2 to redisExpiresConfiguration(600L),
    RedisConstants.TEST_KEY3 to redisExpiresConfiguration(600L),
  )

  private fun redisExpiresConfiguration(ttl: Long): RedisCacheConfiguration {
    return RedisCacheConfiguration.defaultCacheConfig(Thread.currentThread().contextClassLoader)
      .entryTtl(Duration.ofSeconds(ttl))
      .serializeValuesWith(
        RedisSerializationContext.SerializationPair.fromSerializer(
          customRedisSerializer
        )
      )
  }
}