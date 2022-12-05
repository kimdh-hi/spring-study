package com.toy.springcacheex.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import redis.embedded.RedisServer
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@Configuration
class EmbeddedRedisConfig(
  private val redisProperties: RedisProperties
) {

  private val log = LoggerFactory.getLogger(javaClass)

  lateinit var redisServer: RedisServer

  @Bean
  @ConditionalOnProperty(value = ["env"], havingValue = "local")
  fun redisServer(redisProperties: RedisProperties): RedisServer {
    val port = redisProperties.port
    return RedisServer(port)
  }

  @PostConstruct
  fun startRedisServer() {
    redisServer = RedisServer(redisProperties.port)
    try {
      redisServer.start()
    } catch (e: Exception) {
      log.error("Embedded redis error")
    }
  }

  @PreDestroy
  fun stopRedisServer() {
    redisServer.stop()
  }
}